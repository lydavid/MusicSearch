package ly.david.mbjc.ui.artist.releasegroups

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.api.BrowseReleaseGroupsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.artist.ArtistReleaseGroup
import ly.david.data.persistence.artist.ArtistReleaseGroupDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityViewModel
import ly.david.ui.common.releasegroup.ReleaseGroupsPagedList

@HiltViewModel
internal class ReleaseGroupsByArtistViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    private val relationDao: RelationDao,
    releaseGroupDao: ReleaseGroupDao,
    releaseGroupsPagedList: ReleaseGroupsPagedList,
) : ReleaseGroupsByEntityViewModel(
    relationDao = relationDao,
    releaseGroupDao = releaseGroupDao,
    releaseGroupsPagedList = releaseGroupsPagedList
) {

    override suspend fun browseReleaseGroupsByEntity(entityId: String, offset: Int): BrowseReleaseGroupsResponse {
        return musicBrainzApiService.browseReleaseGroupsByArtist(
            artistId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseGroupMusicBrainzModels: List<ReleaseGroupMusicBrainzModel>
    ) {
        artistReleaseGroupDao.insertAll(
            releaseGroupMusicBrainzModels.map { releaseGroup ->
                ArtistReleaseGroup(
                    artistId = entityId,
                    releaseGroupId = releaseGroup.id
                )
            }
        )
    }

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
