package ly.david.musicsearch.data.database.dao

import androidx.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToGenreListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.sort.GenreSortOption
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import lydavidmusicsearchdatadatabase.Genre

class GenreDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.genreQueries

    fun insert(genre: GenreMusicBrainzNetworkModel) {
        genre.run {
            transacter.insert(
                Genre(
                    id = id,
                    name = name,
                    disambiguation = disambiguation.orEmpty(),
                ),
            )
        }
    }

    fun insertAll(genres: List<GenreMusicBrainzNetworkModel>?) {
        transacter.transaction {
            genres?.forEach { genre ->
                insert(genre)
            }
        }
    }

    fun getGenres(
        browseMethod: BrowseMethod,
        query: String,
        sortOption: GenreSortOption,
    ): PagingSource<Int, GenreListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllGenres(
                query = query,
                sortOption = sortOption,
            )
        }

        is BrowseMethod.ByEntity -> {
            getGenresByCollection(
                collectionId = browseMethod.entityId,
                query = query,
                sortOption = sortOption,
            )
        }
    }

    fun observeCountOfGenres(
        browseMethod: BrowseMethod,
        query: String,
    ): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                getCountOfGenresByCollectionQuery(
                    collectionId = browseMethod.entityId,
                    query = query,
                )
            }

            BrowseMethod.All -> {
                getCountOfAllGenres(query = query)
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfAllGenres(
        query: String,
    ): Query<Long> = transacter.getCountOfAllGenres(
        query = "%$query%",
    )

    private fun getGenresByCollection(
        collectionId: String,
        query: String,
        sortOption: GenreSortOption,
    ): PagingSource<Int, GenreListItemModel> = QueryPagingSource(
        countQuery = getCountOfGenresByCollectionQuery(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getGenresByCollection(
                collectionId = collectionId,
                query = "%$query%",
                sortBy = sortOption.order.toLong(),
                limit = limit,
                offset = offset,
                mapper = ::mapToGenreListItemModel,
            )
        },
    )

    private fun getCountOfGenresByCollectionQuery(
        collectionId: String,
        query: String,
    ): Query<Long> = transacter.getNumberOfGenresByCollection(
        collectionId = collectionId,
        query = "%$query%",
    )

    private fun getAllGenres(
        query: String,
        sortOption: GenreSortOption,
    ): PagingSource<Int, GenreListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllGenres(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllGenres(
                query = "%$query%",
                sortBy = sortOption.order.toLong(),
                limit = limit,
                offset = offset,
                mapper = ::mapToGenreListItemModel,
            )
        },
    )
}
