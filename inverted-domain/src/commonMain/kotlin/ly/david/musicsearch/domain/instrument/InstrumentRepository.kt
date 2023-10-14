package ly.david.musicsearch.domain.instrument

import ly.david.musicsearch.data.core.instrument.InstrumentScaffoldModel

interface InstrumentRepository {
    suspend fun lookupInstrument(instrumentId: String): InstrumentScaffoldModel
}
