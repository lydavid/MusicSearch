package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToGenreListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import lydavidmusicsearchdatadatabase.Genre

class GenreDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.genreQueries

    fun insert(genre: GenreMusicBrainzModel) {
        genre.run {
            transacter.insert(
                Genre(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                ),
            )
        }
    }

    fun insertAll(genres: List<GenreMusicBrainzModel>?) {
        transacter.transaction {
            genres?.forEach { genre ->
                insert(genre)
            }
        }
    }

    fun getGenres(
        browseMethod: BrowseMethod,
        query: String,
    ): PagingSource<Int, GenreListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllGenres(
                query = query,
            )
        }

        is BrowseMethod.ByEntity -> {
            getGenresByCollection(
                collectionId = browseMethod.entityId,
                query = query,
            )
        }
    }

    fun observeCountOfAllGenres(): Flow<Int> =
        getCountOfAllGenres(query = "")
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
    ): PagingSource<Int, GenreListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfGenresByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getGenresByCollection(
                collectionId = collectionId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToGenreListItemModel,
            )
        },
    )

    private fun getAllGenres(
        query: String,
    ): PagingSource<Int, GenreListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllGenres(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllGenres(
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToGenreListItemModel,
            )
        },
    )
}
