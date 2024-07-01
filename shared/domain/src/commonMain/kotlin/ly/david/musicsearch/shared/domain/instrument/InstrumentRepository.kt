package ly.david.musicsearch.shared.domain.instrument

import ly.david.musicsearch.core.models.instrument.InstrumentScaffoldModel

interface InstrumentRepository {
    suspend fun lookupInstrument(instrumentId: String): InstrumentScaffoldModel
}
