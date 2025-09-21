package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithCount
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.network.toMusicBrainzEntityType

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
                    thumbnailUrl = urls.thumbnailUrl.trimProtocolAndExtension(),
                    largeUrl = urls.largeUrl.trimProtocolAndExtension(),
                    types = urls.types,
                    comment = urls.comment,
                )
            }
        }
    }

    private fun String.trimProtocolAndExtension(): String = this
        .removePrefix("https://")
        .removePrefix("http://")
        .removeSuffix(".jpg")

    override fun saveImageMetadata(mbidToImageMetadataMap: Map<String, List<ImageMetadata>>) {
        transacter.transaction {
            mbidToImageMetadataMap.forEach { (mbid, imageMetadataList) ->
                saveImageMetadata(mbid, imageMetadataList)
            }
        }
    }

    override fun getFrontImageMetadata(mbid: String): ImageMetadataWithCount? {
        return transacter.getFrontImageMetadata(
            mbid = mbid,
            mapper = ::mapToImageMetadataWithCount,
        ).executeAsOneOrNull()
    }

    override fun getAllImageMetadataById(
        mbid: String,
        query: String,
    ): PagingSource<Int, ImageMetadata> {
        return QueryPagingSource(
            countQuery = transacter.getCountOfAllImageMetadataById(
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

    override fun observeCountOfAllImageMetadata(): Flow<Long> =
        getCountOfAllImageMetadata(query = "")
            .asFlow()
            .mapToOne(coroutineDispatchers.io)

    private fun getCountOfAllImageMetadata(
        query: String,
    ): Query<Long> = transacter.getCountOfAllImageMetadata(
        query = "%$query%",
    )

    override fun getAllImageMetadata(
        query: String,
        sortOption: ImagesSortOption,
    ): PagingSource<Int, ImageMetadata> {
        return QueryPagingSource(
            countQuery = getCountOfAllImageMetadata(
                query = query,
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
}

private fun mapToImageMetadata(
    id: Long,
    thumbnailUrl: String,
    largeUrl: String,
    types: ImmutableList<String>?,
    comment: String?,
) = ImageMetadata(
    imageId = ImageId(id),
    thumbnailUrl = thumbnailUrl,
    largeUrl = largeUrl,
    types = types ?: persistentListOf(),
    comment = comment.orEmpty(),
)

private fun mapToImageMetadataWithCount(
    id: Long,
    thumbnailUrl: String,
    largeUrl: String,
    types: ImmutableList<String>?,
    comment: String?,
    count: Long,
) = ImageMetadataWithCount(
    imageMetadata = ImageMetadata(
        imageId = ImageId(id),
        thumbnailUrl = thumbnailUrl,
        largeUrl = largeUrl,
        types = types ?: persistentListOf(),
        comment = comment.orEmpty(),
    ),
    count = count.toInt(),
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
    imageId = ImageId(id),
    thumbnailUrl = thumbnailUrl,
    largeUrl = largeUrl,
    types = types ?: persistentListOf(),
    comment = comment.orEmpty(),
    mbid = mbid,
    name = name,
    disambiguation = disambiguation,
    entity = entity?.toMusicBrainzEntityType(),
)
