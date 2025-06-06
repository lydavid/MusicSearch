package ly.david.musicsearch.shared.domain.instrument

import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel

interface InstrumentRepository {
    suspend fun lookupInstrument(
        instrumentId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): InstrumentDetailsModel
}
