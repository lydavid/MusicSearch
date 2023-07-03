package ly.david.data.room.image

import androidx.room.Dao
import ly.david.data.coverart.ImageUrlSaver
import ly.david.data.room.BaseDao

@Dao
abstract class MbidImageDao : BaseDao<MbidImage>(), ImageUrlSaver {

    override suspend fun saveUrl(mbid: String, path: String) {
        insert(
            MbidImage(
                mbid = mbid,
                imagePath = path
            )
        )
    }
}
