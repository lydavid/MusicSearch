package ly.david.data

interface Label : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val labelCode: Int?
}
