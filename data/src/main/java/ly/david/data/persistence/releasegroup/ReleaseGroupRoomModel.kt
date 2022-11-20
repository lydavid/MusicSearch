package ly.david.data.persistence.releasegroup

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import ly.david.data.ReleaseGroup
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.artist.ArtistCreditNameRoomModel

// TODO: migrate to 2, then drop, then rename
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


@Entity(
    tableName = "release_groups2",
//    foreignKeys = [
//        ForeignKey(
//            entity = ArtistCreditRoomModel::class,
//            parentColumns = arrayOf("artist_credit_id"),
//            childColumns = arrayOf("id"),
//            onUpdate = ForeignKey.CASCADE,
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
)
data class ReleaseGroupRoomModel2(

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

    @ColumnInfo(name = "artist_credit_id")
    val artistCreditId: Long
) : RoomModel, ReleaseGroup

data class ReleaseGroupWithArtists(
    @Embedded
    val releaseGroup: ReleaseGroupRoomModel,

    // TODO: need view
    //  want this to have a list of ArtistCreditNameRoomModel
    @Relation(
        parentColumn = "id",
        entityColumn = "resource_id"
    )
    val artistCreditNamesWithResources: List<ArtistCreditNamesWithResource>
) : RoomModel

@DatabaseView(
    """
    SELECT acr.resource_id, acn.*
    FROM artist_credits_resources acr
    INNER JOIN artist_credits ac ON ac.id = acr.artist_credit_id
    INNER JOIN artist_credit_names acn ON acn.artist_credit_id = ac.id
"""
)
data class ArtistCreditNamesWithResource(

    @ColumnInfo(name = "resource_id")
    val resourceId: String,

    @Embedded
    val artistCreditNameRoomModel: ArtistCreditNameRoomModel,
)

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

//fun ReleaseGroupMusicBrainzModel.toReleaseGroupRoomModel(
//    artistCreditId: Long
//): ReleaseGroupRoomModel2 =
//    ReleaseGroupRoomModel2(
//        id = id,
//        name = name,
//        firstReleaseDate = firstReleaseDate,
//        disambiguation = disambiguation,
//        primaryType = primaryType,
//        secondaryTypes = secondaryTypes,
//        artistCreditId = artistCreditId
//    )
