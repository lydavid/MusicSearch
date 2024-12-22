package ly.david.musicsearch.data.database.dao

import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImageUrls
import ly.david.musicsearch.data.database.Database

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

    override fun getAllUrlsById(mbid: String): List<ImageUrls> {
        return transacter.getAllUrlsById(
            mbid = mbid,
            mapper = { _, _, thumbnailUrl, largeUrl, types, comment ->
                ImageUrls(
                    thumbnailUrl = thumbnailUrl,
                    largeUrl = largeUrl,
                    types = types ?: persistentListOf(),
                    comment = comment.orEmpty(),
                )
            },
        ).executeAsList()
    }

    override fun deleteAllUrlsById(mbid: String) {
        transacter.deleteAllUrlsById(mbid)
    }

    override fun getNumberOfImagesById(mbid: String): Long {
        return transacter.getNumberOfImagesById(mbid).executeAsOne()
    }
}
