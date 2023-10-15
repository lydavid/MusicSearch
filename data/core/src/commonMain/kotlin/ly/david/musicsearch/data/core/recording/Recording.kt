package ly.david.musicsearch.data.core.recording

import ly.david.musicsearch.data.core.NameWithDisambiguation

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
