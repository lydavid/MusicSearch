package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.musicsearch.data.core.instrument.InstrumentScaffoldModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Instrument

class InstrumentDao(
    database: Database,
) : EntityDao {
    override val transacter = database.instrumentQueries

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
                ),
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

    fun getInstrumentForDetails(instrumentId: String): InstrumentScaffoldModel? {
        return transacter.getInstrument(
            instrumentId,
            mapper = ::toInstrumentScaffoldModel,
        ).executeAsOneOrNull()
    }

    private fun toInstrumentScaffoldModel(
        id: String,
        name: String,
        disambiguation: String?,
        description: String?,
        type: String?,
    ) = InstrumentScaffoldModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        description = description,
        type = type,
    )
}
