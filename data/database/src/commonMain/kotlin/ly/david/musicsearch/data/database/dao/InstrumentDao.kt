package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToInstrumentListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import lydavidmusicsearchdatadatabase.Instrument

class InstrumentDao(
    database: Database,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.instrumentQueries

    fun insert(instrument: InstrumentMusicBrainzNetworkModel) {
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

    fun insertAll(instruments: List<InstrumentMusicBrainzNetworkModel>) {
        transacter.transaction {
            instruments.forEach { instrument ->
                insert(instrument)
            }
        }
    }

    fun getInstrumentForDetails(instrumentId: String): InstrumentDetailsModel? {
        return transacter.getInstrumentForDetails(
            instrumentId = instrumentId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String?,
        description: String?,
        type: String?,
        lastUpdated: Instant?,
    ) = InstrumentDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        description = description,
        type = type,
        lastUpdated = lastUpdated ?: Clock.System.now(),
    )

    fun delete(id: String) {
        transacter.delete(id)
    }

    fun getInstruments(
        browseMethod: BrowseMethod,
        query: String,
    ): PagingSource<Int, InstrumentListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllInstruments(
                query = query,
            )
        }

        is BrowseMethod.ByEntity -> {
            getInstrumentsByCollection(
                collectionId = browseMethod.entityId,
                query = query,
            )
        }
    }

    fun observeCountOfInstruments(browseMethod: BrowseMethod): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                collectionEntityDao.getCountOfEntitiesByCollectionQuery(
                    collectionId = browseMethod.entityId,
                )
            }

            else -> {
                getCountOfAllInstruments(query = "")
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfAllInstruments(
        query: String,
    ): Query<Long> = transacter.getCountOfAllInstruments(
        query = "%$query%",
    )

    private fun getInstrumentsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, InstrumentListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfInstrumentsByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getInstrumentsByCollection(
                collectionId = collectionId,
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
        countQuery = getCountOfAllInstruments(
            query = query,
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
