package ly.david.data.room.relation

import androidx.room.ColumnInfo
import ly.david.data.network.MusicBrainzEntity

/**
 * Maps from [RelationRoomModel].
 */
data class RelationTypeCount(
    @ColumnInfo(name = "linked_resource") val linkedEntity: MusicBrainzEntity,
    @ColumnInfo(name = "count") val count: Int,
)
