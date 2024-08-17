package ly.david.musicsearch.shared.domain.recording

import ly.david.musicsearch.shared.domain.NameWithDisambiguation

/**
 * Defines common properties between domain, network and persistence model.
 */
interface Recording : NameWithDisambiguation {
    val id: String
    override val name: String
    val firstReleaseDate: String?
    override val disambiguation: String
    val length: Int?
    val video: Boolean?
}
