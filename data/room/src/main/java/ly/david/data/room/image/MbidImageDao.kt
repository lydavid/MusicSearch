package ly.david.data.room.image

import androidx.room.Dao
import ly.david.data.image.ImageUrlSaver
import ly.david.data.room.BaseDao

@Dao
abstract class MbidImageDao : BaseDao<MbidImage>(), ImageUrlSaver {

    override suspend fun saveUrl(mbid: String, thumbnailUrl: String, largeUrl: String) {
        insert(
            MbidImage(
                mbid = mbid,
                thumbnailUrl = thumbnailUrl,
                largeUrl = largeUrl
            )
        )
    }
}
