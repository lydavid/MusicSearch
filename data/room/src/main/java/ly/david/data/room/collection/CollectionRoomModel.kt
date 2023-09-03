package ly.david.data.room.collection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.musicbrainz.CollectionMusicBrainzModel
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.getCount
import ly.david.data.room.RoomModel

@Entity(
    tableName = "collection"
)
data class CollectionRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "is_remote") val isRemote: Boolean = false,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "entity") val entity: MusicBrainzEntity,
    @ColumnInfo(name = "type") val type: String? = null,
    @ColumnInfo(name = "type_id") val typeId: String? = null,
    @ColumnInfo(name = "entity_count") val entityCount: Int = 0,
) : RoomModel

fun CollectionMusicBrainzModel.toCollectionRoomModel(): CollectionRoomModel {
    return CollectionRoomModel(
        id = id,
        isRemote = true,
        name = name,
        entity = entity,
        type = type,
        typeId = typeId,
        entityCount = getCount()
    )
}
