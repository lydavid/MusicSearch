package ly.david.data.room.artist.releases

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * This is a junction table linking artists and releases.
 */
@Entity(
    tableName = "artist_release",
    primaryKeys = ["artist_id", "release_id"]
)
data class ArtistRelease(
    @ColumnInfo(name = "artist_id")
    val artistId: String,

    @ColumnInfo(name = "release_id")
    val releaseId: String,
)
