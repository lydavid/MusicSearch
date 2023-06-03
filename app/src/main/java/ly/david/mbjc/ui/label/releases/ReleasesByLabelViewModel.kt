package ly.david.mbjc.ui.label.releases

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.api.BrowseReleasesResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.label.ReleaseLabelDao
import ly.david.data.persistence.label.toReleaseLabels
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseForListItem
import ly.david.mbjc.ui.common.ReleasesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class ReleasesByLabelViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseLabelDao: ReleaseLabelDao,
    private val relationDao: RelationDao,
    releaseDao: ReleaseDao,
    pagedList: PagedList<ReleaseForListItem, ReleaseListItemModel>,
) : ReleasesByEntityViewModel(
    relationDao = relationDao,
    releaseDao = releaseDao,
    pagedList = pagedList
) {

    override suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse {
        return musicBrainzApiService.browseReleasesByLabel(
            labelId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>
    ) {
        releaseLabelDao.insertAll(
            releaseMusicBrainzModels.flatMap { release ->
                release.labelInfoList?.toReleaseLabels(releaseId = release.id, labelId = entityId).orEmpty()
            }
        )
    }

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        releaseLabelDao.withTransaction {
            releaseLabelDao.deleteReleasesByLabel(resourceId)
            releaseLabelDao.deleteLabelReleaseLinks(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RELEASE)
        }
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, ReleaseForListItem> = when {
        query.isEmpty() -> {
            releaseLabelDao.getReleasesByLabel(resourceId)
        }
        else -> {
            releaseLabelDao.getReleasesByLabelFiltered(
                labelId = resourceId,
                query = "%$query%"
            )
        }
    }
}
