package ly.david.mbjc.ui.recording

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Recording
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.LookupHistory
import ly.david.mbjc.data.persistence.LookupHistoryDao
import ly.david.mbjc.data.persistence.recording.RecordingDao
import ly.david.mbjc.data.persistence.toRecordingRoomModel
import ly.david.mbjc.ui.navigation.Destination

@Singleton
internal class RecordingRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
//    private val releaseDao: ReleaseDao,
//    private val mediumDao: MediumDao,
//    private val trackDao: TrackDao,
    private val recordingDao: RecordingDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var recording: Recording? = null

    suspend fun lookupRecording(recordingId: String): Recording =
        recording ?: run {

            // Use cached model.
            // TODO: if we previously store recordings, but not their relations, then need to make api call again
            //  let's try to avoid that though
            val recordingRoomModel = recordingDao.getRecording(recordingId)
            if (recordingRoomModel != null) {
                incrementOrInsertLookupHistory(recordingRoomModel)
                return recordingRoomModel
            }

            val musicBrainzRecording = musicBrainzApiService.lookupRecording(recordingId)
            recordingDao.insert(musicBrainzRecording.toRecordingRoomModel())

            // TODO: insert relations
//            musicBrainzRecording.media?.forEach { medium ->
//                val mediumId = mediumDao.insert(medium.toMediumRoomModel(musicBrainzRecording.id))
//
//                trackDao.insertAll(medium.tracks?.map { it.toTrackRoomModel(mediumId) } ?: emptyList())
//            }

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
