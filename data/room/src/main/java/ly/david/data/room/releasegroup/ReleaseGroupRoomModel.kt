package ly.david.data.room.releasegroup

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.ReleaseGroup
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.room.RoomModel

@Entity(
    tableName = "release_group",
)
data class ReleaseGroupRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "title") override val name: String = "",
    @ColumnInfo(name = "first_release_date") override val firstReleaseDate: String = "",
    @ColumnInfo(name = "disambiguation") override val disambiguation: String = "",
    @ColumnInfo(name = "primary_type") override val primaryType: String? = null,
    @ColumnInfo(name = "primary_type_id") val primaryTypeId: String? = null,
    @ColumnInfo(name = "secondary_types") override val secondaryTypes: List<String>? = null,
    @ColumnInfo(name = "secondary_type_ids") val secondaryTypeIds: List<String>? = null,
) : RoomModel, ReleaseGroup

fun ReleaseGroupMusicBrainzModel.toRoomModel(): ReleaseGroupRoomModel =
    ReleaseGroupRoomModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        primaryTypeId = primaryTypeId,
        secondaryTypes = secondaryTypes,
        secondaryTypeIds = secondaryTypeIds
    )
