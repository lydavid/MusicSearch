package ly.david.musicsearch.shared.domain.releasegroup

import ly.david.musicsearch.shared.domain.NameWithDisambiguation

/**
 * Defines common properties between domain, network and persistence model.
 */
interface ReleaseGroup : NameWithDisambiguation, ReleaseGroupTypes {

    val id: String
    override val name: String
    override val disambiguation: String
    val firstReleaseDate: String

    override val primaryType: String?
    override val secondaryTypes: List<String>?
}
