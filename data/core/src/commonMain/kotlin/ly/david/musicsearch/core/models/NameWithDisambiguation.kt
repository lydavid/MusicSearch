package ly.david.musicsearch.core.models

import ly.david.musicsearch.core.models.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

/**
 * Represents an entity that has a title/name, and disambiguation.
 *
 * Currently, every [MusicBrainzEntity] uses this except for URL.
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
