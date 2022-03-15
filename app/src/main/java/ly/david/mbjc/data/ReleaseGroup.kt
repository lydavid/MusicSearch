package ly.david.mbjc.data

import ly.david.mbjc.data.domain.UiReleaseGroup

/**
 * Defines common properties between network and persistence model.
 */
interface ReleaseGroup: NameWithDisambiguation, ReleaseGroupTypes {

    val id: String
    override val name: String
    val firstReleaseDate: String
    override val disambiguation: String

    override val primaryType: String?
    override val secondaryTypes: List<String>?
}

// TODO: ordering actually has null first. Right now, that would push bootlegs to the top, so we're not doing it.
private val primaryPrecedence = listOf("Album", "Single", "EP", "Broadcast", "Other")

// TODO: Album + Compilation would be followed by Album + Compilation + Live + Remix, etc
private val secondaryPrecedence = listOf(
    listOf(), listOf("Compilation")
)

private fun Int.moveNotFoundToEnd() = if (this == -1) Int.MAX_VALUE else this

fun List<UiReleaseGroup>.sortAndGroupByTypes(): Map<String, List<UiReleaseGroup>> =
    this.sortedWith(
        compareBy<UiReleaseGroup> {
            primaryPrecedence.indexOf(it.primaryType).moveNotFoundToEnd()
        }.thenBy {
            secondaryPrecedence.indexOf(it.secondaryTypes).moveNotFoundToEnd()
        }
    )
        .groupBy { it.getDisplayTypes() }
