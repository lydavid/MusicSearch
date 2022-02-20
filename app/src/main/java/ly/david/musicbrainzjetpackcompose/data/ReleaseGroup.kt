package ly.david.musicbrainzjetpackcompose.data

import com.squareup.moshi.Json
import ly.david.musicbrainzjetpackcompose.preferences.NO_TYPE

data class ReleaseGroup(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "first-release-date") val firstReleaseDate: String,
    @Json(name = "disambiguation") val disambiguation: String = "",

    // album, single, ep, other
    @Json(name = "primary-type") val primaryType: String? = null,
    @Json(name = "primary-type-id") val primaryTypeId: String? = null,

    // audio drama, audiobook, broadcast, compilation, dj-mix, interview, live, mixtape/street, remix, soundtrack, spokenword
    @Json(name = "secondary-types") val secondaryTypes: List<String>? = null,
    @Json(name = "secondary-type-ids") val secondaryTypeIds: List<String>? = null,

    // inc=artists
    @Json(name = "artist-credit") val artistCredits: List<ArtistCredit>? = null,

    // inc=label
    @Json(name = "label-info") val labelInfoList: List<LabelInfo>? = null,

    // inc=media
    @Json(name = "media") val media: List<Medium>? = null,

    // lookup only, inc=releases
    @Json(name = "releases") val releases: List<Release>? = null,
)

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
