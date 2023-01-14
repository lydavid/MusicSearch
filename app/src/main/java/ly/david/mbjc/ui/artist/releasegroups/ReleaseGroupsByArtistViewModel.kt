package ly.david.mbjc.ui.artist.releasegroups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.api.coverart.CoverArtArchiveApiService
import ly.david.data.network.api.coverart.ReleaseGroupCoverArt
import ly.david.data.persistence.artist.ArtistReleaseGroup
import ly.david.data.persistence.artist.ArtistReleaseGroupDao
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.data.persistence.releasegroup.ReleaseGroupForListItem
import ly.david.mbjc.ui.common.paging.BrowseSortableResourceUseCase
import ly.david.mbjc.ui.common.paging.SortablePagedList
import ly.david.mbjc.ui.releasegroup.ReleaseGroupsPagedList

@HiltViewModel
internal class ReleaseGroupsByArtistViewModel @Inject constructor(
    private val releaseGroupsPagedList: ReleaseGroupsPagedList,
    private val musicBrainzApiService: MusicBrainzApiService,
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    override val releaseGroupDao: ReleaseGroupDao,
    private val relationDao: RelationDao,
    override val coverArtArchiveApiService: CoverArtArchiveApiService
) : ViewModel(),
    SortablePagedList<ListItemModel> by releaseGroupsPagedList,
    BrowseSortableResourceUseCase<ReleaseGroupForListItem>,
    ReleaseGroupCoverArt {

    init {
        releaseGroupsPagedList.scope = viewModelScope
        releaseGroupsPagedList.useCase = this
    }

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleaseGroupsByArtist(
            artistId = resourceId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.RELEASE_GROUP,
                    localCount = response.releaseGroups.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(
                resourceId,
                MusicBrainzResource.RELEASE_GROUP,
                response.releaseGroups.size
            )
        }

        val musicBrainzReleaseGroups = response.releaseGroups
        releaseGroupDao.insertAllReleaseGroupsWithArtistCredits(musicBrainzReleaseGroups)
        artistReleaseGroupDao.insertAll(
            musicBrainzReleaseGroups.map { releaseGroup ->
                ArtistReleaseGroup(
                    artistId = resourceId,
                    releaseGroupId = releaseGroup.id
                )
            }
        )

        return musicBrainzReleaseGroups.size
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE_GROUP)?.remoteCount

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE_GROUP)?.localCount ?: 0

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        artistReleaseGroupDao.deleteReleaseGroupsByArtist(resourceId)
        relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RELEASE_GROUP)
    }

    override fun getLinkedResourcesPagingSource(resourceId: String, query: String, sorted: Boolean) = when {
        sorted && query.isEmpty() -> {
            artistReleaseGroupDao.getReleaseGroupsByArtistSorted(resourceId)
        }
        sorted -> {
            artistReleaseGroupDao.getReleaseGroupsByArtistFilteredSorted(
                artistId = resourceId,
                query = "%$query%"
            )
        }
        query.isEmpty() -> {
            artistReleaseGroupDao.getReleaseGroupsByArtist(resourceId)
        }
        else -> {
            artistReleaseGroupDao.getReleaseGroupsByArtistFiltered(
                artistId = resourceId,
                query = "%$query%"
            )
        }
    }
}
