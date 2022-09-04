package ly.david.mbjc.data

internal interface Event : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?

    // Doesn't seem like API returns a type-id unlike other resources
    val type: String?
    val lifeSpan: LifeSpan?
}
