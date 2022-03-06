package ly.david.mbjc.data.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.ReleaseGroup
import ly.david.mbjc.data.UiReleaseGroup
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
    override val title: String = "",
    @ColumnInfo(name = "first-release-date")
    override val firstReleaseDate: String = "",
    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String = "",

    @ColumnInfo(name = "primary-type")
    override val primaryType: String? = null,

    @ColumnInfo(name = "secondary-types")
    override val secondaryTypes: List<String>? = null,
) : ReleaseGroup

// TODO: do we really need to build this many mappers? it gives us the most control but maybe we can generalize?
fun MusicBrainzReleaseGroup.toRoomReleaseGroup(): RoomReleaseGroup {
    return RoomReleaseGroup(
        id = id,
        title = title,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,

        primaryType = primaryType,
        secondaryTypes = secondaryTypes
    )
}

fun UiReleaseGroup.toRoomReleaseGroup(): RoomReleaseGroup {
    return RoomReleaseGroup(
        id = id,
        title = title,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,

        primaryType = primaryType,
        secondaryTypes = secondaryTypes
    )
}
