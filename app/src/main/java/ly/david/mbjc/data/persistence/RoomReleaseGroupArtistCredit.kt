package ly.david.mbjc.data.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import ly.david.mbjc.data.ArtistCredit

// Do this if we need a many-to-many relationship.
// For 1-to-many, we can have a field to reference another entity's id.
/**
 * An [Artist] for a [ReleaseGroup].
 * A release group can have many artists. An artist can have many release groups.
 * An [ArtistCredit] for a [ReleaseGroup] should map to this.
 * Remember [ArtistCredit] can exist for other entities like [Recording]. That will have its own table.
 */
// TODO: this is actually 1-to-many, so we'll want foreignKeys referencing ReleaseGroup and an id for this (auto gen?)
//  a many to many would be a linking table of release group and artist, without all the other data,
//  it's probably not needed
@Entity(
    tableName = "release_groups_artists",
    primaryKeys = ["release_group_id", "artist_id", "order"]
)
data class RoomReleaseGroupArtistCredit(
    @ColumnInfo(name = "release_group_id")
    val releaseGroupId: String,

    @ColumnInfo(name = "artist_id")
    val artistId: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "join_phrase")
    override val joinPhrase: String?,

    @ColumnInfo(name = "order")
    val order: Int
): ArtistCredit
