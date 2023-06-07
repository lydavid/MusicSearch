package ly.david.data.room.relation

import androidx.room.ColumnInfo
import ly.david.data.network.MusicBrainzResource

/**
 * Maps from [RelationRoomModel].
 */
data class RelationTypeCount(
    @ColumnInfo(name = "linked_resource") val linkedResource: MusicBrainzResource,
    @ColumnInfo(name = "count") val count: Int,
)
