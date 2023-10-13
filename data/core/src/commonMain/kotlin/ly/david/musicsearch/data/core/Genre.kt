package ly.david.musicsearch.data.core

interface Genre : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
}
