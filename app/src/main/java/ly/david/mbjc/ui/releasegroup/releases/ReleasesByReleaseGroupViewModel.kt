package ly.david.mbjc.ui.releasegroup.releases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseWithCreditsAndCountries
import ly.david.data.persistence.release.toRoomModel
import ly.david.data.persistence.releasegroup.ReleasesReleaseGroupsDao
import ly.david.mbjc.ui.common.paging.BrowseResourceUseCase
import ly.david.mbjc.ui.common.paging.PagedList
import ly.david.mbjc.ui.release.ReleasesPagedList

@HiltViewModel
internal class ReleasesByReleaseGroupViewModel @Inject constructor(
    private val releasesPagedList: ReleasesPagedList,
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseDao: ReleaseDao,
    private val relationDao: RelationDao,
    private val releasesReleaseGroupsDao: ReleasesReleaseGroupsDao,
) : ViewModel(),
    PagedList<ReleaseListItemModel> by releasesPagedList, BrowseResourceUseCase<ReleaseWithCreditsAndCountries> {

    init {
        releasesPagedList.scope = viewModelScope
        releasesPagedList.useCase = this
    }

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByReleaseGroup(
            releaseGroupId = resourceId,
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

        val musicBrainzReleases = response.releases
        releaseDao.insertOrUpdate(musicBrainzReleases.map { it.toRoomModel(resourceId) })

        return musicBrainzReleases.size
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE)?.remoteCount

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE)?.localCount ?: 0

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        releasesReleaseGroupsDao.deleteReleasesByReleaseGroup(resourceId)
        relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RELEASE)
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, ReleaseWithCreditsAndCountries> = when {
        query.isEmpty() -> {
            releasesReleaseGroupsDao.getReleasesByReleaseGroup(resourceId)
        }
        else -> {
            releasesReleaseGroupsDao.getReleasesByReleaseGroupFiltered(
                releaseGroupId = resourceId,
                query = "%$query%"
            )
        }
    }
}
