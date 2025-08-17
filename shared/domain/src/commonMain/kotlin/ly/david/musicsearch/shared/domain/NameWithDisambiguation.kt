package ly.david.musicsearch.shared.domain

import ly.david.musicsearch.shared.domain.common.appendOptionalText
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

/**
 * Represents an entity that has a title/name, and disambiguation.
 *
 * Currently, every [MusicBrainzEntityType] uses this except for URL.
 */
interface NameWithDisambiguation {
    val name: String?
    val disambiguation: String?
}

/**
 * Get name, and optionally disambiguation if it's not null or empty.
 */
fun NameWithDisambiguation.getNameWithDisambiguation(): String =
    name.orEmpty().appendOptionalText(disambiguation)
