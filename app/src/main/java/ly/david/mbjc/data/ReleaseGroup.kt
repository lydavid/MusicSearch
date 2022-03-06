package ly.david.mbjc.data

import ly.david.mbjc.data.network.NO_TYPE
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
