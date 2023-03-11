package ly.david.data.persistence.collection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.network.CollectionMusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.getCount
import ly.david.data.persistence.RoomModel

@Entity(
    tableName = "collection"
)
data class CollectionRoomModel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "mbid") val mbid: String? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "entity") val entity: MusicBrainzResource,
    @ColumnInfo(name = "type") val type: String? = null,
    @ColumnInfo(name = "type-id") val typeId: String? = null,
    @ColumnInfo(name = "entity-count", defaultValue = "0") val entityCount: Int = 0,
): RoomModel

fun CollectionMusicBrainzModel.toCollectionRoomModel(): CollectionRoomModel {
    return CollectionRoomModel(
        mbid = id,
        name = name,
        entity = entity,
        type = type,
        typeId = typeId,
        entityCount = getCount()
    )
}
