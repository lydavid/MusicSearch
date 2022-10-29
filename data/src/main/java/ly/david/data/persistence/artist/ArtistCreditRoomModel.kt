package ly.david.data.persistence.artist

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
//@Entity(
//    tableName = "artist_credits",
//    primaryKeys = ["release_id", "order"]
//)
//data class ArtistCreditRoomModel(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id")
//    val id: Long = 0,
//
//    @ColumnInfo(name = "artist_id")
//    val artistId: String,
//
//    @ColumnInfo(name = "name")
//    override val name: String,
//
//    @ColumnInfo(name = "join_phrase")
//    override val joinPhrase: String? = null,
//
//    @ColumnInfo(name = "order")
//    val order: Int
//) : ArtistCredit
