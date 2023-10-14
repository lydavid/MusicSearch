package ly.david.musicsearch.domain.recording

import ly.david.musicsearch.data.core.recording.RecordingScaffoldModel

interface RecordingRepository {
    suspend fun lookupRecording(recordingId: String): RecordingScaffoldModel
}
