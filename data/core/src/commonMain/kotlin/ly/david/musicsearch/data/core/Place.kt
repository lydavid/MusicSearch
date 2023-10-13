package ly.david.musicsearch.data.core

interface Place : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val address: String
    val type: String?
    val coordinates: Coordinates?
    val lifeSpan: LifeSpan?
}
