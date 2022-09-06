package ly.david.mbjc.data.persistence.work

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.Work
import ly.david.mbjc.data.network.WorkMusicBrainzModel
import ly.david.mbjc.data.persistence.RoomModel

@Entity(tableName = "works")
internal data class WorkRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String?,

    @ColumnInfo(name = "type")
    override val type: String?,

    @ColumnInfo(name = "language")
    override val language: String?,

//    override val languages: List<String>?,
) : RoomModel, Work

internal fun WorkMusicBrainzModel.toWorkRoomModel() =
    WorkRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        language = language,
//        languages = languages
    )
