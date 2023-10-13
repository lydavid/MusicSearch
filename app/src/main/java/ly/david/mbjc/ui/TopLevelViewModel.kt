package ly.david.mbjc.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.network.resourceUriPlural
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.musicbrainz.auth.MusicBrainzAuthStore
import ly.david.data.musicbrainz.auth.MusicBrainzOAuthInfo
import ly.david.musicsearch.data.database.INSERTION_FAILED_DUE_TO_CONFLICT
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.domain.history.DeleteLookupHistory
import ly.david.musicsearch.domain.history.MarkLookupHistoryForDeletion
import ly.david.musicsearch.domain.history.UnMarkLookupHistoryForDeletion
import ly.david.musicsearch.data.core.listitem.CollectionListItemModel
import ly.david.musicsearch.domain.listitem.toCollectionListItemModel
import ly.david.musicsearch.domain.paging.MusicBrainzPagingConfig
import ly.david.ui.settings.AppPreferences
import lydavidmusicsearchdatadatabase.Collection
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber
import java.util.UUID

@KoinViewModel
internal class TopLevelViewModel(
    val appPreferences: AppPreferences,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,

    private val collectionDao: CollectionDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val musicBrainzApi: MusicBrainzApi,

    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val authRequest: AuthorizationRequest,
    private val authService: AuthorizationService,
    private val clientAuth: ClientAuthentication,

    private val markLookupHistoryForDeletion: MarkLookupHistoryForDeletion,
    private val unMarkLookupHistoryForDeletion: UnMarkLookupHistoryForDeletion,
    private val deleteLookupHistory: DeleteLookupHistory,
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
                    collectionDao.getAllCollections(entity = it)
                }
            ).flow.map { pagingData ->
                pagingData.map(Collection::toCollectionListItemModel)
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
            collectionDao.insertLocal(
                Collection(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    entity = entity,
                    entity_count = 1,
                    is_remote = false,
                    type = null,
                    type_id = null,
                )
            )
        }
    }

    suspend fun addToCollectionAndGetResult(collectionId: String): RemoteResult {
        var result = RemoteResult()

        val collection = collectionDao.getCollection(collectionId)
        if (collection.is_remote) {
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
                collectionId = collectionId,
                entityId = entityId.value,
            )

            result = if (insertedOneEntry == INSERTION_FAILED_DUE_TO_CONFLICT) {
                RemoteResult("Already in ${collection.name}.")
            } else {
//                collectionDao.update(
//                    collection.copy(entityCount = collection.entityCount + 1)
//                )
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
        if (collection.is_remote) {
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
                musicBrainzAuthStore.saveTokens(
                    accessToken = authState.accessToken.orEmpty(),
                    refreshToken = authState.refreshToken.orEmpty(),
                )

                try {
                    val username = musicBrainzApi.getUserInfo().username ?: return@launch
                    musicBrainzAuthStore.setUsername(username)
                } catch (ex: Exception) {
                    // TODO: snackbar
                    Timber.e("$ex")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val refreshToken = musicBrainzAuthStore.getRefreshToken()
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
                musicBrainzAuthStore.saveTokens("", "")
                musicBrainzAuthStore.setUsername("")
            }
        }
    }

    fun markHistoryAsDeleted(mbid: String) {
        markLookupHistoryForDeletion(mbid)
    }

    fun markAllHistoryAsDeleted() {
        markLookupHistoryForDeletion()
    }

    fun undoDeleteHistory(mbid: String) {
        unMarkLookupHistoryForDeletion(mbid)
    }

    fun undoDeleteAllHistory() {
        unMarkLookupHistoryForDeletion()
    }

    fun deleteHistory(mbid: String) {
        deleteLookupHistory(mbid)
    }

    fun deleteAllHistory() {
        deleteLookupHistory()
    }
}
