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
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.data.auth.MusicBrainzAuthState
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.domain.toCollectionListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.api.MusicBrainzAuthApi
import ly.david.data.network.resourceUriPlural
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.persistence.INSERTION_FAILED_DUE_TO_CONFLICT
import ly.david.data.persistence.collection.CollectionDao
import ly.david.data.persistence.collection.CollectionEntityDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.collection.CollectionRoomModel
import ly.david.data.persistence.collection.CollectionWithEntities
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import timber.log.Timber

class MusicBrainzLoginContract(
    private val authService: AuthorizationService,
    private val authRequest: AuthorizationRequest
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
    private val collectionDao: CollectionDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val musicBrainzApiService: MusicBrainzApiService,

    private val musicBrainzAuthState: MusicBrainzAuthState,
    private val musicBrainzAuthApi: MusicBrainzAuthApi,
    private val authRequest: AuthorizationRequest,
    private val authService: AuthorizationService,
    private val clientAuth: ClientAuthentication
) : ViewModel() {

    val entity: MutableStateFlow<MusicBrainzResource> = MutableStateFlow(MusicBrainzResource.ARTIST)

    @OptIn(ExperimentalCoroutinesApi::class)
    val collections: Flow<PagingData<CollectionListItemModel>> =
        entity.flatMapLatest {
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                pagingSourceFactory = {
                    collectionDao.getAllCollectionsOfType(it)
                }
            ).flow.map { pagingData ->
                pagingData.map { collection: CollectionWithEntities ->
                    collection.toCollectionListItemModel()
                }
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    fun setEntity(entity: MusicBrainzResource) {
        this.entity.value = entity
    }

    fun addToCollection(collectionId: Long, entityId: String) {
        viewModelScope.launch {
            collectionEntityDao.withTransaction {
                val insertedOneEntry = collectionEntityDao.insert(
                    CollectionEntityRoomModel(
                        id = collectionId,
                        entityId = entityId
                    )
                )

                // TODO: if we insert into remote collection, then refresh collection, we lose our collection_entity
                //  which increments the collection despite it not adding to MB
                //  MB returns 200 even if entity already exists in collection
                //  Possible workaround: do not manually increment for remote collection -> but would need to GET its new count
                //      or we can fetch all of the collection's content before inserting -> could take a long time if its large
                //  For remote collections, we could not show its count until we fetch its content? strange ux
                //  Could be confusing ux to have such a big difference between local and remote collections
                //      Could have a "Remote" and "Local" filter pills at the top
                if (insertedOneEntry != INSERTION_FAILED_DUE_TO_CONFLICT) {
                    val collection = collectionDao.getCollection(collectionId)
                    val collectionMBid = collection.mbid
                    if (collectionMBid != null) {
                        musicBrainzApiService.uploadToCollection(
                            collectionId = collectionMBid,
                            resourceUriPlural = entity.value.resourceUriPlural,
                            mbids = entityId
                        )
                    }
                    collectionDao.update(
                        collection.copy(entityCount = collection.entityCount + 1)
                    )
                }
            }
        }
    }

    fun createNewCollection(name: String, entity: MusicBrainzResource) {
        viewModelScope.launch {
            collectionDao.insert(
                CollectionRoomModel(
                    name = name,
                    entity = entity
                )
            )
        }
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
                musicBrainzAuthState.setAuthState(authState)

                try {
                    val username = musicBrainzAuthApi.getUserInfo().username ?: return@launch
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
            val authState = musicBrainzAuthState.getAuthState() ?: return@launch
            try {
                musicBrainzAuthApi.logout(
                    token = authState.refreshToken.orEmpty()
                )
            } catch (ex: Exception) {
                // TODO: snackbar
                Timber.e("$ex")
            } finally {
                musicBrainzAuthState.setAuthState(null)
                musicBrainzAuthState.setUsername("")
            }
        }
    }
}
