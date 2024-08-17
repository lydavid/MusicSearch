package ly.david.musicsearch.shared.domain.recording

import ly.david.musicsearch.shared.domain.recording.RecordingDetailsModel

interface RecordingRepository {
    suspend fun lookupRecording(recordingId: String): RecordingDetailsModel
}
