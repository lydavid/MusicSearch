package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImageUrls

class MbidImageDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao, ImageUrlDao {
    override val transacter = database.mbid_imageQueries

    override fun saveUrls(
        mbid: String,
        imageUrls: List<ImageUrls>,
    ) {
        transacter.transaction {
            imageUrls.forEach { urls ->
                transacter.insert(
                    mbid = mbid,
                    thumbnailUrl = urls.thumbnailUrl,
                    largeUrl = urls.largeUrl,
                    types = urls.types,
                    comment = urls.comment,
                )
            }
        }
    }

    override fun getFrontCoverUrl(mbid: String): ImageUrls? {
        return transacter.getFrontCoverUrl(
            mbid = mbid,
            mapper = ::mapToImageUrls,
        ).executeAsOneOrNull()
    }

    override fun getAllUrlsById(
        mbid: String,
        query: String
    ): PagingSource<Int, ImageUrls> {
        return QueryPagingSource(
            countQuery = transacter.getAllUrlsByIdCount(
                mbid = mbid,
                query = "%$query%",
            ),
            transacter = transacter,
            context = coroutineDispatchers.io,
            queryProvider = { limit, offset ->
                transacter.getAllUrlsById(
                    mbid = mbid,
                    query = "%$query%",
                    limit = limit,
                    offset = offset,
                    mapper = ::mapToImageUrls,
                )
            }
        )
    }

    override fun getAllUrls(
        query: String,
    ): PagingSource<Int, ImageUrls> {
        return QueryPagingSource(
            countQuery = transacter.getAllUrlsCount(
                query = "%$query%",
            ),
            transacter = transacter,
            context = coroutineDispatchers.io,
            queryProvider = { limit, offset ->
                transacter.getAllUrls(
                    query = "%$query%",
                    limit = limit,
                    offset = offset,
                    mapper = ::mapToImageUrls,
                )
            }
        )
    }

    override fun deleteAllUrlsById(mbid: String) {
        transacter.deleteAllUrlsById(mbid)
    }

    override fun getNumberOfImagesById(mbid: String): Long {
        return transacter.getNumberOfImagesById(mbid).executeAsOne()
    }
}

private fun mapToImageUrls(
    id: Long,
    thumbnailUrl: String,
    largeUrl: String,
    types: ImmutableList<String>?,
    comment: String?,
) = ImageUrls(
    databaseId = id,
    thumbnailUrl = thumbnailUrl,
    largeUrl = largeUrl,
    types = types ?: persistentListOf(),
    comment = comment.orEmpty(),
)
