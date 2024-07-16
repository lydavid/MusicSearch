package ly.david.musicsearch.shared.domain.instrument

import ly.david.musicsearch.core.models.instrument.InstrumentDetailsModel

interface InstrumentRepository {
    suspend fun lookupInstrument(instrumentId: String): InstrumentDetailsModel
}
