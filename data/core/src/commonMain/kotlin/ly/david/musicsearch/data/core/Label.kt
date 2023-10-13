package ly.david.musicsearch.data.core

interface Label : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val labelCode: Int?
}
