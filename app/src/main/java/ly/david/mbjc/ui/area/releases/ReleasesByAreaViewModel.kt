package ly.david.mbjc.ui.area.releases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.domain.toReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.data.persistence.area.ReleasesCountriesDao
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseWithCreditsAndCountries
import ly.david.data.persistence.release.toRoomModel
import ly.david.mbjc.ui.common.paging.BrowseResourceUseCase
import ly.david.mbjc.ui.common.paging.PagedList
import ly.david.mbjc.ui.common.paging.PagedListImpl

@HiltViewModel
internal class ReleasesByAreaViewModel @Inject constructor(
    private val pagedListImpl: PagedListImpl<ReleaseWithCreditsAndCountries, ReleaseListItemModel>,
    private val musicBrainzApiService: MusicBrainzApiService,
    private val relationDao: RelationDao,
    private val releasesCountriesDao: ReleasesCountriesDao,
    private val releaseDao: ReleaseDao,
) : ViewModel(),
    PagedList<ReleaseListItemModel> by pagedListImpl,
    BrowseResourceUseCase<ReleaseWithCreditsAndCountries, ReleaseListItemModel> {

    init {
        pagedListImpl.scope = viewModelScope
        pagedListImpl.useCase = this
    }

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByArea(
            areaId = resourceId,
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
        releasesCountriesDao.insertAll(
            releaseMusicBrainzModels.map { release ->
                ReleaseCountry(
                    releaseId = release.id,
                    countryId = resourceId,
                    date = release.date
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
        releasesCountriesDao.deleteReleasesFromCountry(resourceId)
        relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RELEASE)
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, ReleaseWithCreditsAndCountries> = when {
        query.isEmpty() -> {
            releasesCountriesDao.getReleasesByCountry(resourceId)
        }
        else -> {
            releasesCountriesDao.getReleasesByCountryFiltered(
                areaId = resourceId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: ReleaseWithCreditsAndCountries): ReleaseListItemModel {
        return roomModel.toReleaseListItemModel()
    }
}
