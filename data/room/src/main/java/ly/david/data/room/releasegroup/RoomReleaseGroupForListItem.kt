package ly.david.data.room.releasegroup

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.RoomModel
import ly.david.data.room.image.MbidImage

data class RoomReleaseGroupForListItem(
    @Embedded
    val releaseGroup: ReleaseGroupRoomModel,

    @ColumnInfo("artist_credit_names")
    val artistCreditNames: String?,

    @Relation(
        entity = MbidImage::class,
        parentColumn = "id",
        entityColumn = "mbid",
        projection = ["thumbnail_url"]
    )
    val thumbnailUrl: String?,
) : RoomModel
