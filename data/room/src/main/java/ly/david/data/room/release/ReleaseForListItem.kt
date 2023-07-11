package ly.david.data.room.release

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.RoomModel
import ly.david.data.room.area.releases.ReleaseCountry
import ly.david.data.room.image.MbidImage

data class ReleaseForListItem(
    @Embedded
    val release: ReleaseRoomModel,

    @ColumnInfo("artist_credit_names")
    val artistCreditNames: String?,

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

    @Relation(
        entity = MbidImage::class,
        parentColumn = "id",
        entityColumn = "mbid",
        projection = ["thumbnail_url"]
    )
    val thumbnailUrl: String?,
) : RoomModel
