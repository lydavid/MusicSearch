package ly.david.musicsearch.shared.domain.recording

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import kotlin.time.Instant

interface RecordingRepository {
    suspend fun lookupEntity(
        entityId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): RecordingDetailsModel

    suspend fun getRecordingsByIsrc(isrc: String?): Flow<RecordingsMatchingIsrcResult>
}
