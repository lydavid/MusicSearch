package ly.david.musicsearch.shared.domain.recording

import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel

interface RecordingRepository {
    suspend fun lookupRecording(
        recordingId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): RecordingDetailsModel
}
