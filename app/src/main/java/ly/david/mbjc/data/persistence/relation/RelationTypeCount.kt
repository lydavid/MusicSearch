package ly.david.mbjc.data.persistence.relation

import androidx.room.ColumnInfo
import ly.david.mbjc.data.network.MusicBrainzResource

/**
 * Maps from [RelationRoomModel].
 */
internal data class RelationTypeCount(
    @ColumnInfo(name = "linked_resource") val linkedResource: MusicBrainzResource,
    @ColumnInfo(name = "count") val count: Int,
)
