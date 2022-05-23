package ly.david.mbjc.data

/**
 * Defines common properties between domain, network and persistence model.
 */
internal interface Recording : NameWithDisambiguation {
    val id: String
    override val name: String
    val date: String?
    override val disambiguation: String
    val length: Int?
    val video: Boolean?
}
