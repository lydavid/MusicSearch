package ly.david.mbjc.ui.artist.releases

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.api.BrowseReleasesResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.artist.release.ArtistRelease
import ly.david.data.persistence.artist.release.ArtistReleaseDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseForListItem
import ly.david.mbjc.ui.common.ReleasesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class ReleasesByArtistViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val artistReleaseDao: ArtistReleaseDao,
    private val relationDao: RelationDao,
    releaseDao: ReleaseDao,
    pagedList: PagedList<ReleaseForListItem, ReleaseListItemModel>,
) : ReleasesByEntityViewModel(
    relationDao = relationDao,
    releaseDao = releaseDao,
    pagedList = pagedList
) {

    override suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse {
        return musicBrainzApiService.browseReleasesByArtist(
            artistId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>
    ) {
        artistReleaseDao.insertAll(
            releaseMusicBrainzModels.map { release ->
                ArtistRelease(
                    artistId = entityId,
                    releaseId = release.id
                )
            }
        )
    }

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

    // TODO: ideal for selecting labels. though where would those labels be shown?
    override fun postFilter(listItemModel: ReleaseListItemModel): Boolean {
        return true
    }
}
