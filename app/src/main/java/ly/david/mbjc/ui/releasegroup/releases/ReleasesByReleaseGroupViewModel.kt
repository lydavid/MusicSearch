package ly.david.mbjc.ui.releasegroup.releases

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.ReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.api.BrowseReleasesResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.ReleaseForListItem
import ly.david.data.room.releasegroup.releases.ReleaseReleaseGroup
import ly.david.data.room.releasegroup.releases.ReleaseReleaseGroupDao
import ly.david.ui.common.release.ReleasesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class ReleasesByReleaseGroupViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<ReleaseForListItem, ReleaseListItemModel>,
    releaseDao: ReleaseDao,
) : ReleasesByEntityViewModel(
    relationDao = relationDao,
    releaseDao = releaseDao,
    pagedList = pagedList
) {

    override suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse {
        return musicBrainzApiService.browseReleasesByReleaseGroup(
            releaseGroupId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>
    ) {
        releaseReleaseGroupDao.insertAll(
            releaseMusicBrainzModels.map { release ->
                ReleaseReleaseGroup(
                    releaseId = release.id,
                    releaseGroupId = entityId
                )
            }
        )
    }

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        releaseReleaseGroupDao.withTransaction {
            releaseReleaseGroupDao.deleteReleasesByReleaseGroup(resourceId)
            releaseReleaseGroupDao.deleteReleaseReleaseGroupLinks(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RELEASE)
        }
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, ReleaseForListItem> = when {
        query.isEmpty() -> {
            releaseReleaseGroupDao.getReleasesByReleaseGroup(resourceId)
        }
        else -> {
            releaseReleaseGroupDao.getReleasesByReleaseGroupFiltered(
                releaseGroupId = resourceId,
                query = "%$query%"
            )
        }
    }
}
