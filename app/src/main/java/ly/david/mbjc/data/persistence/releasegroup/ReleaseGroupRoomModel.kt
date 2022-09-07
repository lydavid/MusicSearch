package ly.david.mbjc.data.persistence.releasegroup

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.ReleaseGroup
import ly.david.mbjc.data.network.ReleaseGroupMusicBrainzModel
import ly.david.mbjc.data.persistence.RoomModel

@Entity(
    tableName = "release_groups",
//    foreignKeys = []
)
internal data class ReleaseGroupRoomModel(

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
     * The total number of releases in this release group in Music Brainz's database.
     *
     * We track this number so that we know whether or not we've collected them all in our local database.
     *
     * When not set, it means we have not queried for the number of releases in this release group.
     */
    @ColumnInfo(name = "release_count")
    val releaseCount: Int? = null,

    /**
     * Flag to determine whether we should fetch their relationships from MB.
     */
    @ColumnInfo(name = "has_default_relations", defaultValue = "false")
    val hasDefaultRelations: Boolean = false,
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

internal fun ReleaseGroupMusicBrainzModel.toReleaseGroupRoomModel(
    hasDefaultRelations: Boolean = false,
): ReleaseGroupRoomModel = ReleaseGroupRoomModel(
    id = id,
    name = name,
    firstReleaseDate = firstReleaseDate,
    disambiguation = disambiguation,
    primaryType = primaryType,
    secondaryTypes = secondaryTypes,
    hasDefaultRelations = hasDefaultRelations
)
