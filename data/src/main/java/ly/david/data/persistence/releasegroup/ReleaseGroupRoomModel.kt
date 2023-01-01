package ly.david.data.persistence.releasegroup

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.ReleaseGroup
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.persistence.RoomModel

@Entity(
    tableName = "release_group",
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

    /**
     * Whether this release group has a cover art.
     * - `null`: Don't know.
     */
    @ColumnInfo(name = "has_cover_art", defaultValue = "null") val hasCoverArt: Boolean? = null,

    /**
     * May be one of:
     * - `null`: Have not requested cover art
     * - Empty: Requested but did not find any
     * - string url: URL to cover art
     */
    @ColumnInfo(name = "cover_art_url", defaultValue = "null")
    val coverArtUrl: String? = null,
) : RoomModel, ReleaseGroup

fun ReleaseGroupMusicBrainzModel.toRoomModel(): ReleaseGroupRoomModel =
    ReleaseGroupRoomModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes,
    )
