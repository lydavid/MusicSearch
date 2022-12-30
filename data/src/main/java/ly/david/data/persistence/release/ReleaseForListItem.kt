package ly.david.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.area.ReleaseCountry

data class ReleaseForListItem(
    @Embedded
    val release: ReleaseRoomModel,

    @ColumnInfo("artist_credit_names")
    val artistCreditNames: String?,

    // TODO: we've lost the ability to filter on this field
    @Relation(
        entity = ReleaseFormatTrackCount::class,
        parentColumn = "id",
        entityColumn = "releaseId",
        projection = ["format", "trackCount"]
    )
    val formatTrackCounts: List<FormatTrackCount>,

    @Relation(
        parentColumn = "id",
        entityColumn = "release_id",
    )
    val releaseCountries: List<ReleaseCountry>,
) : RoomModel
