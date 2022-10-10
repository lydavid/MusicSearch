package ly.david.mbjc.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ly.david.mbjc.data.ArtistCredit
import ly.david.mbjc.data.Release
import ly.david.mbjc.data.network.RecordingMusicBrainzModel

// TODO: Seems pretty inefficient to have a separate table for release groups, releases, and recordings
//  should look into simplifying later
/**
 * An artist's credit for a [Release].
 * A release group can have many artists credits. It can even have the same artist listed twice under different names.
 *
 * An [ArtistCredit] for a [Release] should map to this.
 *
 * Remember [ArtistCredit] can exist for other entities like [RecordingMusicBrainzModel]. That will have its own table.
 */
@Entity(
    tableName = "releases_artists",
    primaryKeys = ["release_id", "order"],
    foreignKeys = [
        ForeignKey(
            entity = ReleaseRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("release_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class ReleaseArtistCreditRoomModel(
    @ColumnInfo(name = "release_id")
    val releaseId: String,

    @ColumnInfo(name = "artist_id")
    val artistId: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "join_phrase")
    override val joinPhrase: String? = null,

    @ColumnInfo(name = "order")
    val order: Int
) : ArtistCredit
