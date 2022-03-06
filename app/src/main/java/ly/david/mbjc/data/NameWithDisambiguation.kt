package ly.david.mbjc.data

import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

/**
 * Represents an entity that has a title/name, and disambiguation.
 */
interface NameWithDisambiguation {
    val name: String
    val disambiguation: String
}

/**
 * Get name, and optionally disambiguation if it's not empty.
 */
fun NameWithDisambiguation.getNameWithDisambiguation(): String =
    name + disambiguation.transformThisIfNotNullOrEmpty { " ($it)" }
