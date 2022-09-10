package ly.david.mbjc.data.persistence.area

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.Area
import ly.david.mbjc.data.LifeSpan
import ly.david.mbjc.data.network.AreaMusicBrainzModel
import ly.david.mbjc.data.persistence.RoomModel

@Entity(tableName = "areas")
internal data class AreaRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String?,

    @ColumnInfo(name = "type")
    override val type: String?,

    @Embedded
    override val lifeSpan: LifeSpan?,

    /**
     * Flag to determine whether we should fetch their relationships from MB.
     */
    @ColumnInfo(name = "has_default_relations", defaultValue = "false")
    val hasDefaultRelations: Boolean = false,
) : RoomModel, Area

internal fun AreaMusicBrainzModel.toAreaRoomModel() =
    AreaRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = lifeSpan
    )
