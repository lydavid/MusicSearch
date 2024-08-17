package ly.david.musicsearch.shared.domain.instrument

import ly.david.musicsearch.shared.domain.instrument.InstrumentDetailsModel

interface InstrumentRepository {
    suspend fun lookupInstrument(instrumentId: String): InstrumentDetailsModel
}
