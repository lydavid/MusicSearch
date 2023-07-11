package ly.david.mbjc.ui.work.recordings

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.RecordingListItemModel
import ly.david.data.domain.listitem.toRecordingListItemModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.api.BrowseRecordingsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.recording.RecordingDao
import ly.david.data.room.recording.RecordingForListItem
import ly.david.data.room.recording.toRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.work.recordings.RecordingWork
import ly.david.data.room.work.recordings.RecordingWorkDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class RecordingsByWorkViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val recordingWorkDao: RecordingWorkDao,
    private val relationDao: RelationDao,
    private val recordingDao: RecordingDao,
    pagedList: PagedList<RecordingForListItem, RecordingListItemModel>,
) : BrowseEntitiesByEntityViewModel<RecordingForListItem, RecordingListItemModel, RecordingMusicBrainzModel, BrowseRecordingsResponse>(
    byEntity = MusicBrainzEntity.RECORDING,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseRecordingsResponse {
        return musicBrainzApiService.browseRecordingsByWork(
            workId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        musicBrainzModels: List<RecordingMusicBrainzModel>,
    ) {
        recordingDao.insertAll(musicBrainzModels.map { it.toRoomModel() })
        recordingWorkDao.insertAll(
            musicBrainzModels.map { recording ->
                RecordingWork(
                    recordingId = recording.id,
                    workId = entityId
                )
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        recordingWorkDao.deleteRecordingsByWork(entityId)
        relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RECORDING)
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, RecordingForListItem> = when {
        query.isEmpty() -> {
            recordingWorkDao.getRecordingsByWork(entityId)
        }
        else -> {
            recordingWorkDao.getRecordingsByWorkFiltered(
                workId = entityId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: RecordingForListItem): RecordingListItemModel {
        return roomModel.toRecordingListItemModel()
    }
}
