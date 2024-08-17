package ly.david.musicsearch.shared.domain.recording

interface RecordingRepository {
    suspend fun lookupRecording(recordingId: String): RecordingDetailsModel
}
