package ly.david.mbjc.ui

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.data.domain.history.LookupHistoryRepository
import ly.david.data.domain.listitem.CollectionListItemModel
import ly.david.data.domain.listitem.toCollectionListItemModel
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.network.MusicBrainzAuthState
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.core.network.RecoverableNetworkException
import ly.david.data.network.api.MusicBrainzApi
import ly.david.data.network.api.MusicBrainzOAuthInfo
import ly.david.data.core.network.resourceUriPlural
import ly.david.data.room.INSERTION_FAILED_DUE_TO_CONFLICT
import ly.david.data.room.collection.CollectionDao
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.collection.CollectionRoomModel
import ly.david.data.room.collection.CollectionWithEntities
import ly.david.ui.settings.AppPreferences
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import timber.log.Timber

class MusicBrainzLoginContract(
    private val authService: AuthorizationService,
    private val authRequest: AuthorizationRequest,
) : ActivityResultContract<Unit, MusicBrainzLoginContract.Result>() {

    data class Result(
        val response: AuthorizationResponse?,
        val exception: AuthorizationException?,
    )

    override fun createIntent(context: Context, input: Unit): Intent {
        return authService.getAuthorizationRequestIntent(authRequest)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result {
        val response = intent?.run { AuthorizationResponse.fromIntent(intent) }
        val exception = AuthorizationException.fromIntent(intent)
        return Result(
            response = response,
            exception = exception
        )
    }
}

@HiltViewModel
internal class TopLevelViewModel @Inject constructor(
    val appPreferences: AppPreferences,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,

    private val collectionDao: CollectionDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val musicBrainzApi: MusicBrainzApi,

    private val musicBrainzAuthState: MusicBrainzAuthState,
    private val authRequest: AuthorizationRequest,
    private val authService: AuthorizationService,
    private val clientAuth: ClientAuthentication,

    private val lookupHistoryRepository: LookupHistoryRepository,
) : ViewModel() {

    data class RemoteResult(
        val message: String = "",
        val actionLabel: String? = null,
    )

    private val entity: MutableStateFlow<MusicBrainzEntity> = MutableStateFlow(MusicBrainzEntity.ARTIST)
    private val entityId: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val collections: Flow<PagingData<CollectionListItemModel>> =
        entity.flatMapLatest {
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                pagingSourceFactory = {
                    collectionDao.getAllCollectionsOfType(it)
                }
            ).flow.map { pagingData ->
                pagingData.map(CollectionWithEntities::toCollectionListItemModel)
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    fun setEntity(entity: MusicBrainzEntity) {
        this.entity.value = entity
    }

    fun setEntityId(entityId: String) {
        this.entityId.value = entityId
    }

    fun createNewCollection(name: String, entity: MusicBrainzEntity) {
        viewModelScope.launch {
            collectionDao.insert(
                CollectionRoomModel(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    entity = entity
                )
            )
        }
    }

    suspend fun addToCollectionAndGetResult(collectionId: String): RemoteResult {
        var result = RemoteResult()

        val collection = collectionDao.getCollection(collectionId)
        if (collection.isRemote) {
            try {
                musicBrainzApi.uploadToCollection(
                    collectionId = collectionId,
                    resourceUriPlural = entity.value.resourceUriPlural,
                    mbids = entityId.value
                )
            } catch (ex: RecoverableNetworkException) {
                val userFacingError = "Failed to add to ${collection.name}. Login has expired."
                Timber.e("$userFacingError $ex")
                return RemoteResult(
                    message = userFacingError,
                    actionLabel = "Login",
                )
            }
        }

        collectionEntityDao.withTransaction {
            val insertedOneEntry = collectionEntityDao.insert(
                CollectionEntityRoomModel(
                    id = collectionId,
                    entityId = entityId.value
                )
            )

            result = if (insertedOneEntry == INSERTION_FAILED_DUE_TO_CONFLICT) {
                RemoteResult("Already in ${collection.name}.")
            } else {
                collectionDao.update(
                    collection.copy(entityCount = collection.entityCount + 1)
                )
                RemoteResult("Added to ${collection.name}.")
            }
        }

        return result
    }

    suspend fun deleteFromCollectionAndGetResult(
        collectionId: String,
        entityId: String,
        entityName: String,
    ): RemoteResult {
        val collection = collectionDao.getCollection(collectionId)
        if (collection.isRemote) {
            try {
                musicBrainzApi.deleteFromCollection(
                    collectionId = collectionId,
                    resourceUriPlural = collection.entity.resourceUriPlural,
                    mbids = entityId
                )
            } catch (ex: RecoverableNetworkException) {
                val userFacingError = "Failed to delete from remote collection ${collection.name}. Login has expired."
                Timber.e("$userFacingError $ex")
                return RemoteResult(
                    message = userFacingError,
                    actionLabel = "Login",
                )
            }
        }

        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteFromCollection(collectionId, entityId)
        }
        return RemoteResult("Deleted $entityName from ${collection.name}.")
    }

    fun getLoginContract() = MusicBrainzLoginContract(authService, authRequest)

    fun performTokenRequest(authorizationResponse: AuthorizationResponse) {
        authService.performTokenRequest(
            authorizationResponse.createTokenExchangeRequest(),
            clientAuth
        ) { response, exception ->
            viewModelScope.launch {
                val authState = AuthState()
                authState.update(response, exception)
                musicBrainzAuthState.saveTokens(
                    accessToken = authState.accessToken.orEmpty(),
                    refreshToken = authState.refreshToken.orEmpty(),
                )

                try {
                    val username = musicBrainzApi.getUserInfo().username ?: return@launch
                    musicBrainzAuthState.setUsername(username)
                } catch (ex: Exception) {
                    // TODO: snackbar
                    Timber.e("$ex")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val refreshToken = musicBrainzAuthState.getRefreshToken()
            if (refreshToken.isNullOrEmpty()) return@launch
            try {
                musicBrainzApi.logout(
                    token = refreshToken,
                    clientId = musicBrainzOAuthInfo.clientId,
                    clientSecret = musicBrainzOAuthInfo.clientSecret
                )
            } catch (ex: Exception) {
                // TODO: snackbar
                Timber.e("$ex")
            } finally {
                musicBrainzAuthState.saveTokens("", "")
                musicBrainzAuthState.setUsername("")
            }
        }
    }

    suspend fun markHistoryAsDeleted(mbid: String) {
        lookupHistoryRepository.markHistoryAsDeleted(mbid)
    }

    suspend fun undoDeleteHistory(mbid: String) {
        lookupHistoryRepository.undoDeleteHistory(mbid)
    }

    suspend fun markAllHistoryAsDeleted() {
        lookupHistoryRepository.markAllHistoryAsDeleted()
    }

    suspend fun undoDeleteAllHistory() {
        lookupHistoryRepository.undoDeleteAllHistory()
    }

    suspend fun deleteHistory(mbid: String) {
        lookupHistoryRepository.deleteHistory(mbid)
    }

    suspend fun deleteAllHistory() {
        lookupHistoryRepository.deleteAllHistory()
    }
}
