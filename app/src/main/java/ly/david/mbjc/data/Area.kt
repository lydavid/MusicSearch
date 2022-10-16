package ly.david.mbjc.data

internal interface Area : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val lifeSpan: LifeSpan?

    // val isoCodes: List<String>?
}
