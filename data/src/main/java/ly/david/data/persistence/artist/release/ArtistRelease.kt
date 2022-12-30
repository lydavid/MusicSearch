package ly.david.data.persistence.artist.release

import androidx.room.ColumnInfo
import androidx.room.Entity

// TODO: we actually don't want to cascade delete in any scenario where another resource screen can delete the same resource
//  so each screen that refreshes should be responsible for dropping linking table
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
