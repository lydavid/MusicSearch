package ly.david.musicsearch.data.database.dao

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImageUrls

class MbidImageDao(
    database: Database,
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
        query: String,
    ): List<ImageUrls> {
        return transacter.getAllUrlsById(
            mbid = mbid,
            query = "%$query%",
            mapper = ::mapToImageUrls,
        ).executeAsList()
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
    mbid: String,
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
