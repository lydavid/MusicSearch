package ly.david.data.persistence.collection

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.network.MusicBrainzResource

@Entity(
    tableName = "collection"
)
data class CollectionRoomModel(

    // TODO: This cannot convert to a MB collection 1-for-1 because we will need them to generate a UUID for us
    //  out of scope at the moment, so don't worry about it
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "entity")
    val entity: MusicBrainzResource

    //    val type: String,

    /**
     * UUID matching MusicBrainz collection types.
     */
//    val typeId: String
)

//fun CollectionMusicBrainzModel.toCollectionRoomModel(): CollectionRoomModel {
//
//}
