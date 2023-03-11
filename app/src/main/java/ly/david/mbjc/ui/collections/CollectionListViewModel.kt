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
import timber.log.Timber

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
                loadPagedResources(username)
            }
        }
    }

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response =
            musicBrainzApiService.browseCollectionsByUser(
                username = resourceId,
                offset = nextOffset,
                include = USER_COLLECTIONS
            )
        Timber.d("${response.collections}")

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.COLLECTION,
                    localCount = response.collections.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(
                resourceId = resourceId,
                browseResource = MusicBrainzResource.COLLECTION,
                additionalOffset = response.collections.size
            )
        }

        val collectionMusicBrainzModels = response.collections
        collectionDao.insertAll(collectionMusicBrainzModels.map { it.toCollectionRoomModel() })

        // TODO: Should we handle a user logging in with multiple accounts?
        //  not right now.
        //  Once a collection is in our local db, we will continue to show it even if they log out.
        //  How do we handle refresh then?
//        artistReleaseDao.insertAll(
//            collectionMusicBrainzModels.map { release ->
//                ArtistRelease(
//                    artistId = resourceId,
//                    releaseId = release.id
//                )
//            }
//        )

        return collectionMusicBrainzModels.size
    }

    override fun transformRoomToListItemModel(roomModel: CollectionWithEntities): CollectionListItemModel {
        return roomModel.toCollectionListItemModel()
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? {
        return relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.COLLECTION)?.remoteCount
    }

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String): Int {
        return relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.COLLECTION)?.localCount ?: 0
    }

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        collectionDao.withTransaction {
            // We do not delete collections who do not have a mbid because they only exist locally
            collectionDao.deleteMusicBrainzCollections()
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.COLLECTION)
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
