package ly.david.mbjc.ui.recording

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Recording
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.history.LookupHistory
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.persistence.recording.RecordingDao
import ly.david.mbjc.data.persistence.recording.toRecordingRoomModel
import ly.david.mbjc.data.persistence.relation.RelationRoomModel
import ly.david.mbjc.data.persistence.relation.toRelationRoomModel

// TODO: place, work will use something like this
@Singleton
internal class RecordingRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val recordingDao: RecordingDao,
    private val relationDao: RelationDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var recording: Recording? = null

    suspend fun lookupRecording(recordingId: String): Recording =
        recording ?: run {

            // Use cached model.
            // TODO: if we haven't stored all relations, then make call again? Only for development
            val recordingRoomModel = recordingDao.getRecording(recordingId)
            if (recordingRoomModel != null) {
                incrementOrInsertLookupHistory(recordingRoomModel)
                return recordingRoomModel
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
            musicBrainzRecording
        }

    private suspend fun incrementOrInsertLookupHistory(recording: Recording) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = recording.name,
                resource = MusicBrainzResource.RECORDING,
                mbid = recording.id
            )
        )
    }
}
