package ly.david.mbjc.data.persistence.artist

import androidx.room.ColumnInfo
import ly.david.mbjc.data.ArtistCredit

// TODO: don't need?
/**
 * A generic [ArtistCredit] model for capturing response from Room.
 * It is agnostic of which resource it was linked with.
 */
internal data class ArtistCreditRoomModel(
    @ColumnInfo(name = "artist_id")
    val artistId: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "join_phrase")
    override val joinPhrase: String? = null,

    @ColumnInfo(name = "order")
    val order: Int
) : ArtistCredit
