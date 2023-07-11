package ly.david.data

interface Instrument : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val description: String?
    val type: String?
}
