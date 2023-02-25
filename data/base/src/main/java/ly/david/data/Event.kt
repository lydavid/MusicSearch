package ly.david.data

interface Event : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String? // Doesn't seem like Search API returns a type-id unlike other resources
    val time: String?
    val cancelled: Boolean?
    val lifeSpan: LifeSpan?
}
