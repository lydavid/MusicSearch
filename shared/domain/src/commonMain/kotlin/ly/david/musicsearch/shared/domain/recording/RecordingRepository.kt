package ly.david.musicsearch.shared.domain.recording

import ly.david.musicsearch.core.models.recording.RecordingScaffoldModel

interface RecordingRepository {
    suspend fun lookupRecording(recordingId: String): RecordingScaffoldModel
}
