package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.Recording
import ly.david.data.domain.RecordingUiModel
import ly.david.data.domain.toRecordingUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.history.LookupHistory
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.recording.RecordingDao
import ly.david.data.persistence.recording.toRecordingRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel

// TODO: place, work will use something like this
@Singleton
class RecordingRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val recordingDao: RecordingDao,
    private val relationDao: RelationDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var recording: RecordingUiModel? = null

    suspend fun lookupRecording(recordingId: String): RecordingUiModel =
        recording ?: run {

            // Use cached model.
            // TODO: if we haven't stored all relations, then make call again? Only for development
            val recordingRoomModel = recordingDao.getRecording(recordingId)
            if (recordingRoomModel != null) {
                incrementOrInsertLookupHistory(recordingRoomModel)
                return recordingRoomModel.toRecordingUiModel()
            }

            val musicBrainzRecording = musicBrainzApiService.lookupRecording(recordingId)
            recordingDao.insert(musicBrainzRecording.toRecordingRoomModel())

            val recordingRelations = mutableListOf<RelationRoomModel>()
            musicBrainzRecording.relations?.forEachIndexed { index, relationMusicBrainzModel ->
                relationMusicBrainzModel.toRelationRoomModel(
                    resourceId = recordingId,
                    order = index
                )?.let { relationRoomModel ->
                    recordingRelations.add(relationRoomModel)
                }
            }
            relationDao.insertAll(recordingRelations)

            incrementOrInsertLookupHistory(musicBrainzRecording)
            musicBrainzRecording.toRecordingUiModel()
        }

    private suspend fun incrementOrInsertLookupHistory(recording: Recording) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                title = recording.name,
                resource = MusicBrainzResource.RECORDING,
                mbid = recording.id
            )
        )
    }
}
