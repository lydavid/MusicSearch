package ly.david.mbjc.ui.area.releases

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.api.BrowseReleasesResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.data.persistence.area.ReleaseCountryDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseForListItem
import ly.david.mbjc.ui.common.ReleasesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class ReleasesByAreaViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseCountryDao: ReleaseCountryDao,
    private val relationDao: RelationDao,
    releaseDao: ReleaseDao,
    pagedList: PagedList<ReleaseForListItem, ReleaseListItemModel>,
) : ReleasesByEntityViewModel(
    relationDao = relationDao,
    releaseDao = releaseDao,
    pagedList = pagedList
) {

    override suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse {
        return musicBrainzApiService.browseReleasesByArea(
            areaId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>
    ) {
        releaseCountryDao.insertAll(
            releaseMusicBrainzModels.map { release ->
                ReleaseCountry(
                    releaseId = release.id,
                    countryId = entityId,
                    date = release.date
                )
            }
        )
    }

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        releaseCountryDao.withTransaction {
            releaseCountryDao.deleteReleasesByCountry(resourceId)
            releaseCountryDao.deleteArtistReleaseLinks(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RELEASE)
        }
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, ReleaseForListItem> = when {
        query.isEmpty() -> {
            releaseCountryDao.getReleasesByCountry(resourceId)
        }
        else -> {
            releaseCountryDao.getReleasesByCountryFiltered(
                areaId = resourceId,
                query = "%$query%"
            )
        }
    }
}
