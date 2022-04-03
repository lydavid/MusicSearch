package ly.david.mbjc.data

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
