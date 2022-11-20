package ly.david.data.persistence.artist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ly.david.data.ArtistCredit

/**
 * Stores the artist credits of release groups, releases and recordings.
 *
 * We currently don't store track artists, but they might differ from a release.
 * They usually match the recording artist.
 *
 * Need to represent a many-to-many relationship between artists and any of the above tables.
 * It may make sense to have separate tables for linking each of these tables,
 * just so that we can cascade delete.
 */
@Entity(
    tableName = "artist_credit_names",
    primaryKeys = ["artist_credit_id", "position"],
    foreignKeys = [
        ForeignKey(
            entity = ArtistCreditRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("artist_credit_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ArtistCreditNameRoomModel(
    @ColumnInfo(name = "artist_credit_id")
    val artistCreditId: Long,

    @ColumnInfo(name = "position")
    val position: Int,

    @ColumnInfo(name = "artist_id")
    val artistId: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "join_phrase")
    override val joinPhrase: String? = null
) : ArtistCredit

// TODO: instead of linking table with each of track/release/rg/rec, we just need them to each have a reference to this table
@Entity(
    tableName = "artist_credits",
    indices = [Index(value = ["name"], unique = true)]
)
data class ArtistCreditRoomModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    // The full artist credit name, with all artist names and join phrases combined
    @ColumnInfo(name = "name")
    val name: String,

    // TODO: don't put this here
    //  or else, we can't reference the same artist credit between release group, release, etc
    //  However, MB doesn't have a linking table between artist credits and each table
    //  how do they do it?
    //  We could make THIS the linking table
    //  in which case, id should not be autogen
    //  If we accept redundant name in this table, then we can search this table for name (which would be all names + joinprhases)
    //  if it exists, use its id and the new resource id
    //  otherwise insert new
//    @ColumnInfo(name = "resource_id")
//    val resourceId: String,

//    @Relation(
//        parentColumn = "id",
//        entityColumn = "artist_credit_id"
//    )
//    val names: List<ArtistCreditNameRoomModel>
)

@Entity(
    tableName = "artist_credits_resources",
    primaryKeys = ["artist_credit_id", "resource_id"],
    // TODO: can't FK to multiple tables directly?
    foreignKeys = [
        ForeignKey(
            entity = ArtistCreditRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("artist_credit_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ArtistCreditResource(
    @ColumnInfo(name = "artist_credit_id")
    val artistCreditId: Long,

    @ColumnInfo(name = "resource_id")
    val resourceId: String
)

data class ArtistCreditUiModel(
    val position: Int,
    val artistId: String,
    override val name: String,
    override val joinPhrase: String? = null
) : ArtistCredit
