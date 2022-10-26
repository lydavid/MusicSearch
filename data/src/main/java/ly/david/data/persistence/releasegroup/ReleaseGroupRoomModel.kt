package ly.david.data.persistence.releasegroup

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.ReleaseGroup
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.persistence.RoomModel

@Entity(
    tableName = "release_groups",
)
data class ReleaseGroupRoomModel(

    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,
    @ColumnInfo(name = "title")
    override val name: String = "",
    @ColumnInfo(name = "first_release_date")
    override val firstReleaseDate: String = "",
    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String = "",

    @ColumnInfo(name = "primary_type")
    override val primaryType: String? = null,

    @ColumnInfo(name = "secondary_types")
    override val secondaryTypes: List<String>? = null,
) : RoomModel, ReleaseGroup

//@Fts4(contentEntity = RoomReleaseGroup::class)
//@Entity(tableName = "release_groups_fts_table")
//data class ReleaseGroupFts(
//    @ColumnInfo(name = "title")
//    val name: String = "",
//
//    @ColumnInfo(name = "first-release-date")
//    val firstReleaseDate: String = "",
//
//    @ColumnInfo(name = "disambiguation")
//    val disambiguation: String = "",
//
//    @ColumnInfo(name = "primary-type")
//    val primaryType: String = "",
//
//    @ColumnInfo(name = "secondary-types")
//    val secondaryTypes: String = ""
//)

fun ReleaseGroupMusicBrainzModel.toReleaseGroupRoomModel(): ReleaseGroupRoomModel =
    ReleaseGroupRoomModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes,
    )
