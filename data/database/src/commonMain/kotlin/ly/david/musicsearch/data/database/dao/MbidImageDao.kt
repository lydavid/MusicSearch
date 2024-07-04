package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.core.models.image.ImageUrlDao
import ly.david.musicsearch.core.models.image.ImageUrls
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
                    thumbnail_url = urls.thumbnailUrl,
                    large_url = urls.largeUrl,
                )
            }
        }
    }

    override fun getAllUrls(mbid: String): List<ImageUrls> {
        return transacter.getAllUrls(
            mbid = mbid,
            mapper = { _, _, thumbnailUrl, largeUrl ->
                ImageUrls(
                    thumbnailUrl = thumbnailUrl,
                    largeUrl = largeUrl,
                )
            },
        ).executeAsList()
    }
}
