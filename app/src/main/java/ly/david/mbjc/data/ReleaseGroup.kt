package ly.david.mbjc.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import ly.david.mbjc.preferences.NO_TYPE
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

@Entity(
    tableName = "release_groups",
    indices = [
        Index(value = ["id"], unique = true)
    ],
//    foreignKeys = []
)
data class ReleaseGroup(

    @PrimaryKey
    @ColumnInfo(name = "id")
    @Json(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    @Json(name = "title")
    val title: String = "",
    @ColumnInfo(name = "first-release-date")
    @Json(name = "first-release-date")
    val firstReleaseDate: String = "",
    @ColumnInfo(name = "disambiguation")
    @Json(name = "disambiguation")
    val disambiguation: String = "",

    // album, single, ep, other
    @ColumnInfo(name = "primary-type")
    @Json(name = "primary-type")
    val primaryType: String? = null,
    @ColumnInfo(name = "primary-type-id")
    @Json(name = "primary-type-id")
    val primaryTypeId: String? = null,

    // TODO: List<String> can be type converted to String separated by , and back
    // audio drama, audiobook, broadcast, compilation, dj-mix, interview, live, mixtape/street, remix, soundtrack, spokenword
    @ColumnInfo(name = "secondary-types")
    @Json(name = "secondary-types")
    val secondaryTypes: List<String>? = null,
    @ColumnInfo(name = "secondary-type-ids")
    @Json(name = "secondary-type-ids")
    val secondaryTypeIds: List<String>? = null,

    // TODO: can we 1-to-many relationship this? possibly embed  name/joinphrase, and reference list of artist id?
    // inc=artists
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
)

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
