package ly.david.mbjc.ui.collections.releases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.domain.toReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.artist.release.ArtistRelease
import ly.david.data.persistence.artist.release.ArtistReleaseDao
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseForListItem
import ly.david.data.persistence.release.toRoomModel
import ly.david.mbjc.ui.common.paging.BrowseResourceUseCase
import ly.david.mbjc.ui.common.paging.IPagedList
import ly.david.mbjc.ui.common.paging.PagedList

// TODO: we will need something similar to ReleasesByArtistViewModel if we sync with MB's collection
//  consider how we can integrate local collections with mb's collections before committing too much
//  - users can create collections in our app without making a mb account
//  - after auth, they can choose to sync their collections to mb
//  - collections without a mb id will be pushed to mb, then the newly created mbid will be saved
//  - collections from mb will be pulled into our db
//  - refreshing a collection will pull data from mb -> but how do we sync? what if user adds on our app, refreshing shouldn't delete it
// TODO: research ANKI syncing
@HiltViewModel
internal class ReleasesByCollectionViewModel @Inject constructor(
    private val pagedList: PagedList<ReleaseForListItem, ReleaseListItemModel>,
    private val musicBrainzApiService: MusicBrainzApiService,
    private val relationDao: RelationDao,
    private val artistReleaseDao: ArtistReleaseDao,
    private val releaseDao: ReleaseDao,
) : ViewModel(),
    IPagedList<ReleaseListItemModel> by pagedList,
    BrowseResourceUseCase<ReleaseForListItem, ReleaseListItemModel> {

    init {
        pagedList.scope = viewModelScope
        pagedList.useCase = this
    }

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByArtist(
            artistId = resourceId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.RELEASE,
                    localCount = response.releases.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(resourceId, MusicBrainzResource.RELEASE, response.releases.size)
        }

        val releaseMusicBrainzModels = response.releases
        releaseDao.insertAll(releaseMusicBrainzModels.map { it.toRoomModel() })
        artistReleaseDao.insertAll(
            releaseMusicBrainzModels.map { release ->
                ArtistRelease(
                    artistId = resourceId,
                    releaseId = release.id
                )
            }
        )

        return releaseMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE)?.remoteCount

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE)?.localCount ?: 0

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        artistReleaseDao.withTransaction {
            artistReleaseDao.deleteReleasesByArtist(resourceId)
            artistReleaseDao.deleteArtistReleaseLinks(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RELEASE)
        }
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, ReleaseForListItem> = when {
        query.isEmpty() -> {
            artistReleaseDao.getReleasesByArtist(resourceId)
        }
        else -> {
            artistReleaseDao.getReleasesByArtistFiltered(
                artistId = resourceId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: ReleaseForListItem): ReleaseListItemModel {
        return roomModel.toReleaseListItemModel()
    }

    override fun postFilter(listItemModel: ReleaseListItemModel): Boolean {
        return true
    }
}
