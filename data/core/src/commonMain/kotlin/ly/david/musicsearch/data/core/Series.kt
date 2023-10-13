package ly.david.musicsearch.data.core

interface Series : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
}
