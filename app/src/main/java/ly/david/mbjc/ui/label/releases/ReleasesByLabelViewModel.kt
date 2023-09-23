package ly.david.mbjc.ui.label.releases

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.label.releases.RoomReleaseLabelDao
import ly.david.data.room.label.releases.toReleaseLabels
import ly.david.data.room.release.RoomReleaseDao
import ly.david.data.room.release.RoomReleaseForListItem
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.ui.common.release.ReleasesByEntityViewModel
import ly.david.ui.common.release.ReleasesPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleasesByLabelViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val releaseLabelDao: RoomReleaseLabelDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    releaseDao: RoomReleaseDao,
    pagedList: ReleasesPagedList,
) : ReleasesByEntityViewModel(
    browseEntityCountDao = browseEntityCountDao,
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
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, RoomReleaseForListItem> = when {
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
