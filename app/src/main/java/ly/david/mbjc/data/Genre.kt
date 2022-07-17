package ly.david.mbjc.data

internal interface Genre : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
}
