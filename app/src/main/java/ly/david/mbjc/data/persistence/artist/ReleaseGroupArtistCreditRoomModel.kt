package ly.david.mbjc.data.persistence.artist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ly.david.mbjc.data.ArtistCredit
import ly.david.mbjc.data.ReleaseGroup
import ly.david.mbjc.data.network.RecordingMusicBrainzModel
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupRoomModel

/**
 * An artist's credit for a [ReleaseGroup].
 * A release group can have many artists credits. It can even have the same artist listed twice under different names.
 *
 * An [ArtistCredit] for a [ReleaseGroup] should map to this.
 *
 * Remember [ArtistCredit] can exist for other entities like [RecordingMusicBrainzModel]. That will have its own table.
 */
@Entity(
    tableName = "release_groups_artists",
    primaryKeys = ["release_group_id", "order"],
    foreignKeys = [
        ForeignKey(
            entity = ReleaseGroupRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("release_group_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class ReleaseGroupArtistCreditRoomModel(
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
) : ArtistCredit
