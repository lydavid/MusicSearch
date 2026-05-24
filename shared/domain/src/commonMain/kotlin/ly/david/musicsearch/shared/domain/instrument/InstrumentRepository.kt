package ly.david.musicsearch.shared.domain.instrument

import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel
import kotlin.time.Instant

interface InstrumentRepository {
    suspend fun lookupEntity(
        entityId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): InstrumentDetailsModel
}
