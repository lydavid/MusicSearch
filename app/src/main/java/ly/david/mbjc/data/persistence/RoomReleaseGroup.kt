package ly.david.mbjc.data.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.ReleaseGroup
import ly.david.mbjc.data.domain.UiReleaseGroup
import ly.david.mbjc.data.network.MusicBrainzReleaseGroup

@Entity(
    tableName = "release_groups",
//    foreignKeys = []
)
data class RoomReleaseGroup(

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
) : RoomData(), ReleaseGroup

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

// TODO: do we really need to build this many mappers? it gives us the most control but maybe we can generalize?
fun MusicBrainzReleaseGroup.toRoomReleaseGroup(): RoomReleaseGroup {
    return RoomReleaseGroup(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,

        primaryType = primaryType,
        secondaryTypes = secondaryTypes
    )
}

fun UiReleaseGroup.toRoomReleaseGroup(): RoomReleaseGroup {
    return RoomReleaseGroup(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,

        primaryType = primaryType,
        secondaryTypes = secondaryTypes
    )
}
