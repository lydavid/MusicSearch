package ly.david.mbjc.ui.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ly.david.data.auth.MusicBrainzAuthState
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.domain.toCollectionListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.CollectionApi.Companion.USER_COLLECTIONS
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.collection.CollectionDao
import ly.david.data.persistence.collection.CollectionWithEntities
import ly.david.data.persistence.collection.toCollectionRoomModel
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.paging.BrowseResourceUseCase
import ly.david.mbjc.ui.common.paging.IPagedList
import ly.david.mbjc.ui.common.paging.PagedList

private const val ONLY_GIVE_ME_LOCAL_COLLECTIONS = "ONLY_GIVE_ME_LOCAL_COLLECTIONS"

@HiltViewModel
internal class CollectionListViewModel @Inject constructor(
    private val pagedList: PagedList<CollectionWithEntities, CollectionListItemModel>,
    private val musicBrainzApiService: MusicBrainzApiService,
    private val musicBrainzAuthState: MusicBrainzAuthState,
    private val collectionDao: CollectionDao,
    private val relationDao: RelationDao,
) : ViewModel(),
    IPagedList<CollectionListItemModel> by pagedList,
    BrowseResourceUseCase<CollectionWithEntities, CollectionListItemModel> {

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

    override fun transformRoomToListItemModel(roomModel: CollectionWithEntities): CollectionListItemModel {
        return roomModel.toCollectionListItemModel()
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
        resourceId: String,
        query: String
    ): PagingSource<Int, CollectionWithEntities> = when {
        query.isEmpty() -> {
            collectionDao.getAllCollections()
        }
        else -> {
            collectionDao.getAllCollectionsFiltered("%$query%")
        }
    }
}
