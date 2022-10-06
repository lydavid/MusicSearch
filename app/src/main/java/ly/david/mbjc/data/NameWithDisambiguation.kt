package ly.david.mbjc.data

import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

/**
 * Represents an entity that has a title/name, and disambiguation.
 *
 * Currently, every [MusicBrainzResource] uses this except for URL.
 */
internal interface NameWithDisambiguation {
    val name: String?
    val disambiguation: String?
}

/**
 * Get name, and optionally disambiguation if it's not null or empty.
 */
internal fun NameWithDisambiguation.getNameWithDisambiguation(): String =
    name.orEmpty() + disambiguation.transformThisIfNotNullOrEmpty { " ($it)" }
