package ly.david.musicsearch.data.core

interface Event : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val time: String?
    val cancelled: Boolean?
    val lifeSpan: LifeSpan?
}
