package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToInstrumentListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzModel
import ly.david.musicsearch.shared.domain.instrument.InstrumentDetailsModel
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import lydavidmusicsearchdatadatabase.Instrument

class InstrumentDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
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

    fun getInstruments(
        entityId: String?,
        entity: MusicBrainzEntity?,
        query: String,
    ): PagingSource<Int, InstrumentListItemModel> = when {
        entityId == null || entity == null -> {
            getAllInstruments(
                query = query,
            )
        }

        else -> {
            getInstrumentsByCollection(
                entityId = entityId,
                query = query,
            )
        }
    }

    private fun getInstrumentsByCollection(
        entityId: String,
        query: String,
    ): PagingSource<Int, InstrumentListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfInstrumentsByCollection(
            collectionId = entityId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getInstrumentsByCollection(
                collectionId = entityId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToInstrumentListItemModel,
            )
        },
    )

    private fun getAllInstruments(
        query: String,
    ): PagingSource<Int, InstrumentListItemModel> = QueryPagingSource(
        countQuery = transacter.getCountOfAllInstruments(
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllInstruments(
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToInstrumentListItemModel,
            )
        },
    )
}
