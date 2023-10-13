package ly.david.musicsearch.data.core.releasegroup

import ly.david.musicsearch.data.core.NameWithDisambiguation

/**
 * Defines common properties between domain, network and persistence model.
 */
interface ReleaseGroup : NameWithDisambiguation, ReleaseGroupTypes {

    val id: String
    override val name: String
    val firstReleaseDate: String
    override val disambiguation: String

    override val primaryType: String?
    override val secondaryTypes: List<String>?
}
