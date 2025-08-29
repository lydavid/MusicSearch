package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToAreaListItemModel
import ly.david.musicsearch.data.database.mapper.mapToArtistListItemModel
import ly.david.musicsearch.data.database.mapper.mapToEventListItemModel
import ly.david.musicsearch.data.database.mapper.mapToInstrumentListItemModel
import ly.david.musicsearch.data.database.mapper.mapToLabelListItemModel
import ly.david.musicsearch.data.database.mapper.mapToPlaceListItemModel
import ly.david.musicsearch.data.database.mapper.mapToRecordingListItemModel
import ly.david.musicsearch.data.database.mapper.mapToReleaseGroupListItemModel
import ly.david.musicsearch.data.database.mapper.mapToReleaseListItemModel
import ly.david.musicsearch.data.database.mapper.mapToSeriesListItemModel
import ly.david.musicsearch.data.database.mapper.mapToWorkListItemModel
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.search.results.SearchResultMetadata

class SearchResultDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.search_resultQueries

    private fun insert(
        entityId: String,
    ) {
        transacter.insert(
            entity_id = entityId,
        )
    }

    fun insertAll(
        entityIds: List<String>,
    ) {
        transacter.transaction {
            entityIds.forEach { entityId ->
                insert(
                    entityId = entityId,
                )
            }
        }
    }

    fun rewriteMetadata(
        entity: MusicBrainzEntityType,
        query: String,
        localCount: Int,
        remoteCount: Int,
    ) {
        withTransaction {
            transacter.removeMetadata()
            transacter.setMetadata(
                entity = entity,
                query = query,
                localCount = localCount,
                remoteCount = remoteCount,
            )
        }
    }

    fun getMetadata(): SearchResultMetadata? = transacter.getMetadata(
        mapper = { entity, query, localCount, remoteCount ->
            SearchResultMetadata(
                entity = entity,
                query = query,
                localCount = localCount ?: 0,
                remoteCount = remoteCount ?: 0,
            )
        },
    ).executeAsOneOrNull()

    fun getSearchResults(
        entity: MusicBrainzEntityType,
    ): PagingSource<Int, ListItemModel> = when (entity) {
        MusicBrainzEntityType.ARTIST -> getArtistSearchResults()
        MusicBrainzEntityType.AREA -> getAreaSearchResults()
        MusicBrainzEntityType.EVENT -> getEventSearchResults()
        MusicBrainzEntityType.INSTRUMENT -> getInstrumentSearchResults()
        MusicBrainzEntityType.LABEL -> getLabelSearchResults()
        MusicBrainzEntityType.PLACE -> getPlaceSearchResults()
        MusicBrainzEntityType.RECORDING -> getRecordingSearchResults()
        MusicBrainzEntityType.RELEASE -> getReleaseSearchResults()
        MusicBrainzEntityType.RELEASE_GROUP -> getReleaseGroupSearchResults()
        MusicBrainzEntityType.SERIES -> getSeriesSearchResults()
        MusicBrainzEntityType.WORK -> getWorkSearchResults()
        MusicBrainzEntityType.COLLECTION,
        MusicBrainzEntityType.GENRE,
        MusicBrainzEntityType.URL,
        -> {
            error(IllegalStateException("No search results stored for $entity"))
        }
    }

    private fun getAreaSearchResults(): PagingSource<Int, ListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfSearchResults(),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAreaSearchResults(
                limit = limit,
                offset = offset,
                mapper = ::mapToAreaListItemModel,
            )
        },
    )

    private fun getArtistSearchResults(): PagingSource<Int, ListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfSearchResults(),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getArtistSearchResults(
                limit = limit,
                offset = offset,
                mapper = ::mapToArtistListItemModel,
            )
        },
    )

    private fun getEventSearchResults(): PagingSource<Int, ListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfSearchResults(),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getEventSearchResults(
                limit = limit,
                offset = offset,
                mapper = ::mapToEventListItemModel,
            )
        },
    )

    private fun getInstrumentSearchResults(): PagingSource<Int, ListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfSearchResults(),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getInstrumentSearchResults(
                limit = limit,
                offset = offset,
                mapper = ::mapToInstrumentListItemModel,
            )
        },
    )

    private fun getLabelSearchResults(): PagingSource<Int, ListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfSearchResults(),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getLabelSearchResults(
                limit = limit,
                offset = offset,
                mapper = ::mapToLabelListItemModel,
            )
        },
    )

    private fun getPlaceSearchResults(): PagingSource<Int, ListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfSearchResults(),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getPlaceSearchResults(
                limit = limit,
                offset = offset,
                mapper = ::mapToPlaceListItemModel,
            )
        },
    )

    private fun getRecordingSearchResults(): PagingSource<Int, ListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfSearchResults(),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getRecordingSearchResults(
                limit = limit,
                offset = offset,
                mapper = ::mapToRecordingListItemModel,
            )
        },
    )

    private fun getReleaseSearchResults(): PagingSource<Int, ListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfSearchResults(),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleaseSearchResults(
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseListItemModel,
            )
        },
    )

    private fun getReleaseGroupSearchResults(): PagingSource<Int, ListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfSearchResults(),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleaseGroupSearchResults(
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseGroupListItemModel,
            )
        },
    )

    private fun getSeriesSearchResults(): PagingSource<Int, ListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfSearchResults(),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getSeriesSearchResults(
                limit = limit,
                offset = offset,
                mapper = ::mapToSeriesListItemModel,
            )
        },
    )

    private fun getWorkSearchResults(): PagingSource<Int, ListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfSearchResults(),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getWorkSearchResults(
                limit = limit,
                offset = offset,
                mapper = ::mapToWorkListItemModel,
            )
        },
    )

    fun removeAll() {
        transacter.removeAll()
    }
}
