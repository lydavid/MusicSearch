package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.coverarts.ImagesSortOption
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.network.toMusicBrainzEntity

class MbidImageDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao, ImageUrlDao {
    override val transacter = database.mbid_imageQueries

    override fun saveImageMetadata(
        mbid: String,
        imageMetadataList: List<ImageMetadata>,
    ) {
        transacter.transaction {
            imageMetadataList.forEach { urls ->
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

    override fun getFrontImageMetadata(mbid: String): ImageMetadata? {
        return transacter.getFrontImageMetadata(
            mbid = mbid,
            mapper = ::mapToImageMetadata,
        ).executeAsOneOrNull()
    }

    override fun getAllImageMetadataById(
        mbid: String,
        query: String,
    ): PagingSource<Int, ImageMetadata> {
        return QueryPagingSource(
            countQuery = transacter.getAllImageMetadataByIdCount(
                mbid = mbid,
                query = "%$query%",
            ),
            transacter = transacter,
            context = coroutineDispatchers.io,
            queryProvider = { limit, offset ->
                transacter.getAllImageMetadataById(
                    mbid = mbid,
                    query = "%$query%",
                    limit = limit,
                    offset = offset,
                    mapper = ::mapToImageMetadata,
                )
            },
        )
    }

    override fun getAllImageMetadata(
        query: String,
        sortOption: ImagesSortOption,
    ): PagingSource<Int, ImageMetadata> {
        return QueryPagingSource(
            countQuery = transacter.getAllImageMetadataCount(
                query = "%$query%",
            ),
            transacter = transacter,
            context = coroutineDispatchers.io,
            queryProvider = { limit, offset ->
                transacter.getAllImageMetadata(
                    query = "%$query%",
                    alphabetically = sortOption == ImagesSortOption.ALPHABETICALLY,
                    alphabeticallyReverse = sortOption == ImagesSortOption.ALPHABETICALLY_REVERSE,
                    recentlyAdded = sortOption == ImagesSortOption.RECENTLY_ADDED,
                    leastRecentlyAdded = sortOption == ImagesSortOption.LEAST_RECENTLY_ADDED,
                    limit = limit,
                    offset = offset,
                    mapper = ::mapToImageMetadata,
                )
            },
        )
    }

    override fun deleteAllImageMetadtaById(mbid: String) {
        transacter.deleteAllImageMetadtaById(mbid)
    }

    override fun getNumberOfImagesById(mbid: String): Long {
        return transacter.getNumberOfImagesById(mbid).executeAsOne()
    }
}

private fun mapToImageMetadata(
    id: Long,
    thumbnailUrl: String,
    largeUrl: String,
    types: ImmutableList<String>?,
    comment: String?,
) = ImageMetadata(
    databaseId = id,
    thumbnailUrl = thumbnailUrl,
    largeUrl = largeUrl,
    types = types ?: persistentListOf(),
    comment = comment.orEmpty(),
)

private fun mapToImageMetadata(
    id: Long,
    thumbnailUrl: String,
    largeUrl: String,
    types: ImmutableList<String>?,
    comment: String?,
    mbid: String? = null,
    name: String? = null,
    disambiguation: String? = null,
    entity: String? = null,
) = ImageMetadata(
    databaseId = id,
    thumbnailUrl = thumbnailUrl,
    largeUrl = largeUrl,
    types = types ?: persistentListOf(),
    comment = comment.orEmpty(),
    mbid = mbid,
    name = name,
    disambiguation = disambiguation,
    entity = entity?.toMusicBrainzEntity(),
)
