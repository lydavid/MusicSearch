package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.shared.domain.NameWithDisambiguation

/**
 * Defines common properties between domain, network and persistence model.
 */
interface Artist : NameWithDisambiguation {
    val id: String
    override val name: String
    val sortName: String
    override val disambiguation: String?
    val type: String?
    val gender: String?
}
