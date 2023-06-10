package ly.david.mbjc.ui.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ly.david.data.auth.MusicBrainzAuthState
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.CollectionApi.Companion.USER_COLLECTIONS
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.collection.CollectionDao
import ly.david.data.room.collection.CollectionWithEntities
import ly.david.data.room.collection.toCollectionRoomModel
import ly.david.data.room.relation.BrowseResourceCount
import ly.david.data.room.relation.RelationDao
import ly.david.ui.settings.AppPreferences

private const val ONLY_GIVE_ME_LOCAL_COLLECTIONS = "ONLY_GIVE_ME_LOCAL_COLLECTIONS"

@HiltViewModel
internal class CollectionListViewModel @Inject constructor(
    val appPreferences: ly.david.ui.settings.AppPreferences,
    private val pagedList: CollectionPagedList,
    private val musicBrainzApiService: MusicBrainzApiService,
    private val musicBrainzAuthState: MusicBrainzAuthState,
    private val collectionDao: CollectionDao,
    private val relationDao: RelationDao,
) : ViewModel(),
    ICollectionPagedList by pagedList,
    BrowseCollectionUseCase<CollectionWithEntities> {

    init {
        pagedList.scope = viewModelScope
        pagedList.useCase = this
    }

    fun getUsernameThenLoadCollections() {
        viewModelScope.launch {
            // Hack: We're using username in place of a UUID
            musicBrainzAuthState.username.collectLatest { username ->
                if (username.isEmpty()) {
                    // This id lets the rest of the functions know to avoid network requests
                    loadPagedResources(ONLY_GIVE_ME_LOCAL_COLLECTIONS)
                } else {
                    loadPagedResources(username)
                }
            }
        }
    }

    fun setShowLocal(show: Boolean) {
        appPreferences.setShowLocalCollections(show)
        updateShowLocal(show)
    }

    fun setShowRemote(show: Boolean) {
        appPreferences.setShowRemoteCollections(show)
        updateShowRemote(show)
    }

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        if (resourceId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return 0

        val response = musicBrainzApiService.browseCollectionsByUser(
            username = resourceId,
            offset = nextOffset,
            include = USER_COLLECTIONS
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.COLLECTION,
                    localCount = response.musicBrainzModels.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(
                resourceId = resourceId,
                browseResource = MusicBrainzResource.COLLECTION,
                additionalOffset = response.musicBrainzModels.size
            )
        }

        val collectionMusicBrainzModels = response.musicBrainzModels
        collectionDao.insertAll(collectionMusicBrainzModels.map { it.toCollectionRoomModel() })

        return collectionMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? {
        if (resourceId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return 0

        return relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.COLLECTION)?.remoteCount
    }

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String): Int {
        if (resourceId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return 0

        return relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.COLLECTION)?.localCount ?: 0
    }

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        if (resourceId == ONLY_GIVE_ME_LOCAL_COLLECTIONS) return

        collectionDao.withTransaction {
            relationDao.deleteAllBrowseResourceCountByRemoteCollections()
            collectionDao.deleteMusicBrainzCollections()
        }
    }

    override fun getLinkedResourcesPagingSource(
        viewState: ICollectionPagedList.ViewModelState
    ): PagingSource<Int, CollectionWithEntities> =
        collectionDao.getAllCollections(
            showLocal = viewState.showLocal,
            showRemote = viewState.showRemote,
            query = "%${viewState.query}%"
        )
}
