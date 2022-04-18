package ly.david.mbjc.ui.recording

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Recording
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.LookupHistory
import ly.david.mbjc.data.persistence.LookupHistoryDao
import ly.david.mbjc.data.persistence.RelationDao
import ly.david.mbjc.data.persistence.RelationRoomModel
import ly.david.mbjc.data.persistence.recording.RecordingDao
import ly.david.mbjc.data.persistence.recording.RecordingRelationDao
import ly.david.mbjc.data.persistence.recording.RecordingRelationRoomModel
import ly.david.mbjc.data.persistence.toRecordingRoomModel
import ly.david.mbjc.data.persistence.toRelationRoomModel
import ly.david.mbjc.ui.navigation.Destination

@Singleton
internal class RecordingRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val recordingDao: RecordingDao,
    private val recordingRelationDao: RecordingRelationDao,
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

            val relations = mutableListOf<RelationRoomModel>()
            val recordingRelations = mutableListOf<RecordingRelationRoomModel>()
            musicBrainzRecording.relations?.forEachIndexed { index, relationMusicBrainzModel ->
                relationMusicBrainzModel.toRelationRoomModel()?.let { relationRoomModel ->
                    relations.add(relationRoomModel)
                    recordingRelations.add(
                        RecordingRelationRoomModel(
                            recordingId = recordingId,
                            linkedResourceId = relationRoomModel.resourceId,
                            order = index
                        )
                    )
                }
            }
            relationDao.insertAllIgnoreDuplicates(relations)
            recordingRelationDao.insertAll(recordingRelations)

            incrementOrInsertLookupHistory(musicBrainzRecording)
            musicBrainzRecording
        }

    private suspend fun incrementOrInsertLookupHistory(recording: Recording) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = recording.name,
                destination = Destination.LOOKUP_RECORDING,
                mbid = recording.id
            )
        )
    }
}
