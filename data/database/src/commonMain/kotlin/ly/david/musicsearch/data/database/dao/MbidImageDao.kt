package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.core.models.image.ImageUrlDao
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Mbid_image

class MbidImageDao(
    database: Database,
) : EntityDao, ImageUrlDao {
    override val transacter = database.mbid_imageQueries

    override fun saveUrl(mbid: String, thumbnailUrl: String, largeUrl: String) {
        transacter.insert(
            Mbid_image(
                mbid = mbid,
                thumbnail_url = thumbnailUrl,
                large_url = largeUrl,
            ),
        )
    }
}
