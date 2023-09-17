package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Instrument

class InstrumentDao(
    database: Database,
) {
    private val transacter = database.instrumentQueries

    fun insert(instrument: InstrumentMusicBrainzModel) {
        instrument.run {
            transacter.insert(
                Instrument(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    description = description,
                    type = type,
                    type_id = typeId,
                )
            )
        }
    }

    fun insertAll(instruments: List<InstrumentMusicBrainzModel>) {
        transacter.transaction {
            instruments.forEach { instrument ->
                insert(instrument)
            }
        }
    }

    fun getInstrument(instrumentId: String): Instrument? {
        return transacter.getInstrument(instrumentId).executeAsOneOrNull()
    }
}
