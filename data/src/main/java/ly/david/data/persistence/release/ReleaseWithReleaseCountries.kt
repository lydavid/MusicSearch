package ly.david.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.area.ReleaseCountry

// TODO: rename
data class ReleaseWithReleaseCountries(
    @Embedded
    val release: ReleaseRoomModel,

    @ColumnInfo("artist_credit_names")
    val artistCreditNames: String?,

    @Relation(
        parentColumn = "id",
        entityColumn = "release_id",
    )
    val releaseEvents: List<ReleaseCountry>,
): RoomModel
