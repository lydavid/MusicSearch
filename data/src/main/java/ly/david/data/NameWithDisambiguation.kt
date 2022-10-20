package ly.david.data

import ly.david.data.common.transformThisIfNotNullOrEmpty
import ly.david.data.network.MusicBrainzResource

/**
 * Represents an entity that has a title/name, and disambiguation.
 *
 * Currently, every [MusicBrainzResource] uses this except for URL.
 */
interface NameWithDisambiguation {
    val name: String?
    val disambiguation: String?
}

/**
 * Get name, and optionally disambiguation if it's not null or empty.
 */
fun NameWithDisambiguation.getNameWithDisambiguation(): String =
    name.orEmpty() + disambiguation.transformThisIfNotNullOrEmpty { " ($it)" }
