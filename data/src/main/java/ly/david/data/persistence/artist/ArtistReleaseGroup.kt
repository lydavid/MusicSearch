package ly.david.data.persistence.artist

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "artists_release_groups",
    primaryKeys = ["artist_id", "release_group_id"]
)
data class ArtistReleaseGroup(
    @ColumnInfo(name = "artist_id")
    val artistId: String,

    @ColumnInfo(name = "release_group_id")
    val releaseGroupId: String,
)
