package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToSeriesListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import lydavidmusicsearchdatadatabase.Series
import lydavidmusicsearchdatadatabase.SeriesQueries
import kotlin.time.Clock
import kotlin.time.Instant

class SeriesDao(
    database: Database,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter: SeriesQueries = database.seriesQueries

    fun insert(series: SeriesMusicBrainzNetworkModel) {
        series.run {
            transacter.insert(
                Series(
                    id = id,
                    name = name,
                    disambiguation = disambiguation.orEmpty(),
                    type = type.orEmpty(),
                    type_id = typeId.orEmpty(),
                ),
            )
        }
    }

    fun insertAll(series: List<SeriesMusicBrainzNetworkModel>) {
        transacter.transaction {
            series.forEach { series ->
                insert(series)
            }
        }
    }

    fun getSeriesForDetails(seriesId: String): SeriesDetailsModel? {
        return transacter.getSeriesForDetails(
            seriesId = seriesId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String,
        type: String,
        lastUpdated: Instant?,
    ) = SeriesDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lastUpdated = lastUpdated ?: Clock.System.now(),
    )

    fun delete(id: String) {
        transacter.delete(id)
    }

    fun getSeries(
        browseMethod: BrowseMethod,
        query: String,
    ): PagingSource<Int, SeriesListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllSeries(
                query = query,
            )
        }

        is BrowseMethod.ByEntity -> {
            getSeriesByCollection(
                entityId = browseMethod.entityId,
                query = query,
            )
        }
    }

    private fun getSeriesByCollection(
        entityId: String,
        query: String,
    ): PagingSource<Int, SeriesListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfSeriesByCollection(
            collectionId = entityId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getSeriesByCollection(
                collectionId = entityId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToSeriesListItemModel,
            )
        },
    )

    fun observeCountOfSeries(browseMethod: BrowseMethod): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                collectionEntityDao.getCountOfEntitiesByCollectionQuery(
                    collectionId = browseMethod.entityId,
                )
            }

            else -> {
                getCountOfAllSeries(query = "")
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfAllSeries(
        query: String,
    ): Query<Long> = transacter.getCountOfAllSeries(
        query = "%$query%",
    )

    private fun getAllSeries(
        query: String,
    ): PagingSource<Int, SeriesListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllSeries(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllSeries(
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToSeriesListItemModel,
            )
        },
    )
}
