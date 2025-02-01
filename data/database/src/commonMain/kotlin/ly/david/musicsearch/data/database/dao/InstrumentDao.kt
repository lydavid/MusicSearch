package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzModel
import ly.david.musicsearch.shared.domain.instrument.InstrumentDetailsModel
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

    fun getInstrumentForDetails(instrumentId: String): InstrumentDetailsModel? {
        return transacter.getInstrument(
            instrumentId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String?,
        description: String?,
        type: String?,
    ) = InstrumentDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        description = description,
        type = type,
    )

    fun delete(id: String) {
        transacter.delete(id)
    }
}
