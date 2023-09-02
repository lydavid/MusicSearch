package ly.david.data.core

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
