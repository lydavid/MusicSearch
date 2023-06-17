package ly.david.data.room.artist

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.room.BaseDao

@Dao
abstract class ArtistDao : BaseDao<ArtistRoomModel>() {

    @Query("SELECT * FROM artist WHERE id = :artistId")
    abstract suspend fun getArtist(artistId: String): ArtistWithAllData?
}
