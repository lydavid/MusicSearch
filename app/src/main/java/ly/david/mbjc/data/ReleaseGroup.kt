package ly.david.mbjc.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import ly.david.mbjc.data.network.MusicBrainzArtistCredit
import ly.david.mbjc.preferences.NO_TYPE
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

/**
 * Defines common properties between network and persistence model.
 */
interface ReleaseGroup {

    val id: String
    val title: String
    val firstReleaseDate: String
    val disambiguation: String

    val primaryType: String?

    val secondaryTypes: List<String>?
}

data class MusicBrainzReleaseGroup(

    @Json(name = "id")
    override val id: String,
    @Json(name = "title")
    override val title: String = "",
    @Json(name = "first-release-date")
    override val firstReleaseDate: String = "",
    @Json(name = "disambiguation")
    override val disambiguation: String = "",

    // album, single, ep, other
    @Json(name = "primary-type")
    override val primaryType: String? = null,
//    @Json(name = "primary-type-id")
//    val primaryTypeId: String? = null, // only useful if we plan to allow linking to type

    // audio drama, audiobook, broadcast, compilation, dj-mix, interview, live, mixtape/street, remix, soundtrack, spokenword
    @Json(name = "secondary-types")
    override val secondaryTypes: List<String>? = null,
//    @Json(name = "secondary-type-ids")
//    val secondaryTypeIds: List<String>? = null,

    // Lookup: inc=artists; Browse: inc=artist-credits
    @Json(name = "artist-credit")
    val artistCredits: List<MusicBrainzArtistCredit>? = null,

    // inc=label
    @Json(name = "label-info")
    val labelInfoList: List<LabelInfo>? = null,

    // inc=media
    @Json(name = "media")
    val media: List<Medium>? = null,

    // lookup only, inc=releases
    @Json(name = "releases")
    val releases: List<Release>? = null,
): ReleaseGroup

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

@Entity(
    tableName = "release_groups",
//    indices = [
//        Index(value = ["id"], unique = true)
//    ],
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

    // TODO: can we 1-to-many relationship this? possibly embed  name/joinphrase, and reference list of artist id?
    // TODO: try foreign key, can we cascade insert new ArtistCredit?
    // inc=artists
    // TODO: we might have reached the point where it no longer makes sense to use a single model
    //  try recommended approach of 3 models: network, database, and domain (app), make a subdir for each under data
    //  while we've been using network model throughout app, if we have a domain specific model that can be mapped from
    //  network/database, we will have more control over it.
    //  We might be able to skip domain model if we tailor our database models to be exactly what we need for each screen
    //  and use appropriate queries to create populate models.
//    @Relation(parentColumn = "id", entityColumn = "id")
//    @Json(name = "artist-credit")
//    val artistCredits: List<ArtistCredit>? = null,
//
//    // inc=label
//    @Json(name = "label-info")
//    val labelInfoList: List<LabelInfo>? = null,
//
//    // inc=media
//    @Json(name = "media")
//    val media: List<Medium>? = null,
//
//    // lookup only, inc=releases
//    @Json(name = "releases")
//    val releases: List<Release>? = null,
): ReleaseGroup

// TODO: We can use a DatabaseView and do something similar to LineWithWordCount
//  to get a Releases count for a Release Group that we've clicked into, and have stored locally
//  otherwise it would be too costly to make additional API calls just for this data.

// TODO: we should have a domain model afterall that our UI should use
//  this model should be mappable from both network or persistence
//  it should include the artist credits as a String already joined

// TODO: Use a View, so that we don't have to store the same Artist Credits for every single release
//  we can just query for it
//@DatabaseView("""
//
//""")
//data class ReleaseGroupWithArtistCredits(
//    @Embedded
//    val releaseGroup: ReleaseGroup
//
//
//)

fun ReleaseGroup.getTitleWithDisambiguation(): String =
    title + disambiguation.transformThisIfNotNullOrEmpty { " ($it)" }

/**
 * Returns [ReleaseGroup]'s primary type concatenated with all secondary types for display.
 */
fun ReleaseGroup.getDisplayTypes(): String {

    var displayTypes = primaryType.orEmpty()

    if (displayTypes.isNotEmpty() && !secondaryTypes.isNullOrEmpty()) {
        displayTypes += " + "
    }
    displayTypes += secondaryTypes?.joinToString(separator = " + ").orEmpty()

    return displayTypes.ifEmpty { NO_TYPE }
}

// TODO: ordering actually has null first. Right now, that would push bootlegs to the top, so we're not doing it.
private val primaryPrecedence = listOf("Album", "Single", "EP", "Broadcast", "Other")

// TODO: Album + Compilation would be followed by Album + Compilation + Live + Remix, etc
private val secondaryPrecedence = listOf(
    listOf(), listOf("Compilation")
)

private fun Int.moveNotFoundToEnd() = if (this == -1) Int.MAX_VALUE else this

fun List<ReleaseGroup>.sortAndGroupByTypes(): Map<String, List<ReleaseGroup>> =
    this.sortedWith(
        compareBy<ReleaseGroup> {
            primaryPrecedence.indexOf(it.primaryType).moveNotFoundToEnd()
        }.thenBy {
            secondaryPrecedence.indexOf(it.secondaryTypes).moveNotFoundToEnd()
        }
    )
        .groupBy { it.getDisplayTypes() }
