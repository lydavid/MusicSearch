package ly.david.data

interface Genre : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
}
