package ly.david.mbjc.ui.label.releases

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.label.releases.ReleaseLabelDao
import ly.david.data.room.label.releases.toReleaseLabels
import ly.david.data.room.relation.RoomRelationDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.ReleaseForListItem
import ly.david.ui.common.release.ReleasesByEntityViewModel
import ly.david.ui.common.release.ReleasesPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleasesByLabelViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val releaseLabelDao: ReleaseLabelDao,
    private val relationDao: RoomRelationDao,
    releaseDao: ReleaseDao,
    pagedList: ReleasesPagedList,
) : ReleasesByEntityViewModel(
    relationDao = relationDao,
    releaseDao = releaseDao,
    pagedList = pagedList,
) {

    override suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse {
        return musicBrainzApi.browseReleasesByLabel(
            labelId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>,
    ) {
        releaseLabelDao.insertAll(
            releaseMusicBrainzModels.flatMap { release ->
                release.labelInfoList?.toReleaseLabels(releaseId = release.id, labelId = entityId).orEmpty()
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        releaseLabelDao.withTransaction {
            releaseLabelDao.deleteReleasesByLabel(entityId)
            releaseLabelDao.deleteLabelReleaseLinks(entityId)
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> = when {
        query.isEmpty() -> {
            releaseLabelDao.getReleasesByLabel(entityId)
        }
        else -> {
            releaseLabelDao.getReleasesByLabelFiltered(
                labelId = entityId,
                query = "%$query%"
            )
        }
    }
}
