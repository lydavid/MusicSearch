package ly.david.mbjc.data

import ly.david.mbjc.data.network.NO_TYPE

internal interface ReleaseGroupTypes {
    val primaryType: String?
    val secondaryTypes: List<String>?
}

/**
 * Returns primary type concatenated with all secondary types for display.
 */
internal fun ReleaseGroupTypes.getDisplayTypes(): String {

    var displayTypes = primaryType.orEmpty()

    if (displayTypes.isNotEmpty() && !secondaryTypes.isNullOrEmpty()) {
        displayTypes += " + "
    }
    displayTypes += secondaryTypes?.joinToString(separator = " + ").orEmpty()

    return displayTypes.ifEmpty { NO_TYPE }
}
