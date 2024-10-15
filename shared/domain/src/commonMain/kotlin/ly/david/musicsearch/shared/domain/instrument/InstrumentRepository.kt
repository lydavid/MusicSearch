package ly.david.musicsearch.shared.domain.instrument

interface InstrumentRepository {
    suspend fun lookupInstrument(
        instrumentId: String,
        forceRefresh: Boolean,
    ): InstrumentDetailsModel
}
