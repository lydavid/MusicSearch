package ly.david.data.persistence.release

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.persistence.RoomModel

data class ReleaseWithFormatTrackCounts(
    @Embedded
    val release: ReleaseRoomModel,

    @Relation(
        entity = ReleaseFormatTrackCount::class,
        parentColumn = "id",
        entityColumn = "releaseId",
        projection = ["format", "trackCount"]
    )
    val formatTrackCounts: List<FormatTrackCount>,
) : RoomModel
