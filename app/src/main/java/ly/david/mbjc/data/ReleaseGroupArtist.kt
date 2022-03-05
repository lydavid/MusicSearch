package ly.david.mbjc.data

import androidx.room.ColumnInfo
import androidx.room.Entity

// Do this if we need a many-to-many relationship.
// For 1-to-many, we can have a field to reference another entity's id.
/**
 * An [Artist] for a [ReleaseGroup].
 * A release group can have many artists. An artist can have many release groups.
 */
@Entity(
    tableName = "release_groups_artists",
    primaryKeys = ["release_group_id", "artist_id"]
)
data class ReleaseGroupArtist(
    @ColumnInfo(name = "release_group_id")
    val releaseGroupId: String,

    @ColumnInfo(name = "artist_id")
    val artistId: String,
)
