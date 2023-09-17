package ly.david.data.room.artist

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao

@Dao
abstract class RoomArtistDao : BaseDao<ArtistRoomModel>() {

    @Transaction
    @Query("SELECT * FROM artist WHERE id = :artistId")
    abstract suspend fun getArtist(artistId: String): ArtistWithAllData?
}
