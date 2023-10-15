package ly.david.musicsearch.core.models.releasegroup

import ly.david.musicsearch.core.models.network.NO_TYPE

interface ReleaseGroupTypes {
    val primaryType: String?
    val secondaryTypes: List<String>?
}

/**
 * Returns primary type concatenated with all secondary types for display.
 */
fun ReleaseGroupTypes.getDisplayTypes(): String {
    var displayTypes = primaryType.orEmpty()

    if (displayTypes.isNotEmpty() && !secondaryTypes.isNullOrEmpty()) {
        displayTypes += " + "
    }
    displayTypes += secondaryTypes?.joinToString(separator = " + ").orEmpty()

    return displayTypes.ifEmpty { NO_TYPE }
}
