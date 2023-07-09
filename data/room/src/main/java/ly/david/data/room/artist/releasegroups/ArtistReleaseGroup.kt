package ly.david.data.room.artist.releasegroups

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * This is a junction table linking artists and release_groups.
 */
@Entity(
    tableName = "artist_release_group",
    primaryKeys = ["artist_id", "release_group_id"]
)
data class ArtistReleaseGroup(
    @ColumnInfo(name = "artist_id")
    val artistId: String,

    @ColumnInfo(name = "release_group_id")
    val releaseGroupId: String,
)
