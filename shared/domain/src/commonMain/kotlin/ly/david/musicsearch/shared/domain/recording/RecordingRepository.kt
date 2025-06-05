package ly.david.musicsearch.shared.domain.recording

import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel

interface RecordingRepository {
    suspend fun lookupRecording(
        recordingId: String,
        forceRefresh: Boolean,
    ): RecordingDetailsModel
}
