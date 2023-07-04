package ly.david.data.room.history

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.image.MbidImage

data class LookupHistoryForListItem(
    @Embedded
    val lookupHistory: LookupHistoryRoomModel,

    @Relation(
        entity = MbidImage::class,
        parentColumn = "mbid",
        entityColumn = "mbid",
        projection = ["thumbnail_url"]
    )
    val thumbnailUrl: String? = null
)
