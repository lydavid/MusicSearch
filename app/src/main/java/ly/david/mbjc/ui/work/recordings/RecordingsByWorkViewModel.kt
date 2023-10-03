package ly.david.mbjc.ui.work.recordings

import androidx.paging.PagingSource
import ly.david.data.core.RecordingForListItem
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.RecordingMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseRecordingsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RecordingWorkDao
import ly.david.musicsearch.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.domain.listitem.toRecordingListItemModel
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.recording.RecordingsPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class RecordingsByWorkViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val recordingWorkDao: RecordingWorkDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val recordingDao: RecordingDao,
    pagedList: RecordingsPagedList,
) : BrowseEntitiesByEntityViewModel<RecordingForListItem, RecordingListItemModel, RecordingMusicBrainzModel, BrowseRecordingsResponse>(
    byEntity = MusicBrainzEntity.RECORDING,
    browseEntityCountDao = browseEntityCountDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseRecordingsResponse {
        return musicBrainzApi.browseRecordingsByWork(
            workId = entityId,
            offset = offset,
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        musicBrainzModels: List<RecordingMusicBrainzModel>,
    ) {
        recordingWorkDao.withTransaction {
            recordingDao.insertAll(musicBrainzModels)
            recordingWorkDao.insertAll(
                workId = entityId,
                recordingIds = musicBrainzModels.map { recording -> recording.id },
            )
        }
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        recordingWorkDao.withTransaction {
            recordingWorkDao.deleteRecordingsByWork(entityId)
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RECORDING)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, RecordingForListItem> =
        recordingWorkDao.getRecordingsByWork(
            workId = entityId,
            query = "%$query%",
        )

    override fun transformDatabaseToListItemModel(databaseModel: RecordingForListItem): RecordingListItemModel {
        return databaseModel.toRecordingListItemModel()
    }
}
