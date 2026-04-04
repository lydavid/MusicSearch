package ly.david.musicsearch.data.database.dao

import androidx.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToImageMetadata
import ly.david.musicsearch.shared.domain.common.trimProtocolAndExtension
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithCount
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithEntity
import ly.david.musicsearch.shared.domain.image.ImageSource
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.image.RawImageMetadata
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.toMusicBrainzEntityType

class MbidImageDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao, ImageUrlDao {
    override val transacter = database.mbid_imageQueries

    override fun saveImageMetadata(
        mbid: String,
        imageMetadataList: List<RawImageMetadata>,
    ) {
        transacter.transaction {
            imageMetadataList.forEach { imageMetadata ->
                val imageSource = imageMetadata.source
                val processedThumbnailUrl = when (imageSource) {
                    ImageSource.INTERNET_ARCHIVE,
                    ImageSource.SPOTIFY,
                    -> imageMetadata.thumbnailUrl.trimProtocolAndExtension()

                    ImageSource.WIKIMEDIA -> imageMetadata.thumbnailUrl
                }
                val processedLargeUrl = when (imageSource) {
                    ImageSource.INTERNET_ARCHIVE,
                    ImageSource.SPOTIFY,
                    -> imageMetadata.largeUrl.trimProtocolAndExtension()

                    ImageSource.WIKIMEDIA -> imageMetadata.largeUrl
                }

                transacter.insert(
                    mbid = mbid,
                    thumbnailUrl = processedThumbnailUrl,
                    largeUrl = processedLargeUrl,
                    types = imageMetadata.types,
                    comment = imageMetadata.comment,
                    source = imageSource,
                )
            }
        }
    }

    override fun saveImageMetadata(mbidToImageMetadataMap: Map<String, List<RawImageMetadata>>) {
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
    ): PagingSource<Int, ImageMetadataWithEntity> {
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
                    mapper = ::mapToImageMetadataWithEntity,
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
    ): PagingSource<Int, ImageMetadataWithEntity> {
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
                    mapper = ::mapToImageMetadataWithEntity,
                )
            },
        )
    }

    override fun deleteAllImageMetadtaById(mbid: String) {
        transacter.deleteAllImageMetadtaById(mbid)
    }
}

private fun mapToImageMetadataWithCount(
    id: Long,
    source: ImageSource,
    thumbnailUrl: String,
    largeUrl: String,
    types: ImmutableList<String>?,
    comment: String?,
    count: Long,
) = ImageMetadataWithCount(
    imageMetadata = mapToImageMetadata(
        id = id,
        source = source,
        thumbnailUrl = thumbnailUrl,
        largeUrl = largeUrl,
        types = types ?: persistentListOf(),
        comment = comment.orEmpty(),
    ),
    count = count.toInt(),
)

private fun mapToImageMetadataWithEntity(
    id: Long,
    source: ImageSource,
    thumbnailUrl: String,
    largeUrl: String,
    types: ImmutableList<String>?,
    comment: String?,
    mbid: String? = null,
    name: String? = null,
    disambiguation: String? = null,
    entity: String? = null,
): ImageMetadataWithEntity {
    val type = entity?.toMusicBrainzEntityType()
    val musicBrainzEntity = if (mbid == null || type == null) {
        null
    } else {
        MusicBrainzEntity(
            id = mbid,
            type = type,
        )
    }
    return ImageMetadataWithEntity(
        imageMetadata = mapToImageMetadata(
            id = id,
            source = source,
            thumbnailUrl = thumbnailUrl,
            largeUrl = largeUrl,
            types = types ?: persistentListOf(),
            comment = comment.orEmpty(),
        ),
        musicBrainzEntity = musicBrainzEntity,
        name = name,
        disambiguation = disambiguation,
    )
}
