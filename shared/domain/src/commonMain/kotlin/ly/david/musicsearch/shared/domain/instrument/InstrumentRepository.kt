package ly.david.musicsearch.shared.domain.instrument

import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel

interface InstrumentRepository {
    suspend fun lookupInstrument(
        instrumentId: String,
        forceRefresh: Boolean,
    ): InstrumentDetailsModel
}
