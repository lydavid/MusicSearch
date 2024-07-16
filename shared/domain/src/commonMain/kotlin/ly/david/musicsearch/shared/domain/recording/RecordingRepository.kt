package ly.david.musicsearch.shared.domain.recording

import ly.david.musicsearch.core.models.recording.RecordingDetailsModel

interface RecordingRepository {
    suspend fun lookupRecording(recordingId: String): RecordingDetailsModel
}
