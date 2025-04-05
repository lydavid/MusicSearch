package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToGenreListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
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
        entityId: String?,
        entity: MusicBrainzEntity?,
        query: String,
    ): PagingSource<Int, GenreListItemModel> = when {
        entityId == null || entity == null -> {
            getAllGenres(
                query = query,
            )
        }

        else -> {
            getGenresByCollection(
                collectionId = entityId,
                query = query,
            )
        }
    }

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
        countQuery = transacter.getCountOfAllGenres(
            query = "%$query%",
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
