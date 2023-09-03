package ly.david.mbjc.ui.releasegroup.releases

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.ReleaseListItemModel
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.ReleaseForListItem
import ly.david.data.room.releasegroup.releases.ReleaseReleaseGroup
import ly.david.data.room.releasegroup.releases.ReleaseReleaseGroupDao
import ly.david.ui.common.paging.PagedList
import ly.david.ui.common.release.ReleasesByEntityViewModel

@HiltViewModel
internal class ReleasesByReleaseGroupViewModel @Inject constructor(
    private val musicBrainzApi: MusicBrainzApi,
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
        return musicBrainzApi.browseReleasesByReleaseGroup(
            releaseGroupId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>,
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

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        releaseReleaseGroupDao.withTransaction {
            releaseReleaseGroupDao.deleteReleasesByReleaseGroup(entityId)
            releaseReleaseGroupDao.deleteReleaseReleaseGroupLinks(entityId)
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> = when {
        query.isEmpty() -> {
            releaseReleaseGroupDao.getReleasesByReleaseGroup(entityId)
        }
        else -> {
            releaseReleaseGroupDao.getReleasesByReleaseGroupFiltered(
                releaseGroupId = entityId,
                query = "%$query%"
            )
        }
    }
}
