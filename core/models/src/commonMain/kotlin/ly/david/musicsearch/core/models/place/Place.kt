package ly.david.musicsearch.core.models.place

import ly.david.musicsearch.core.models.LifeSpan
import ly.david.musicsearch.core.models.NameWithDisambiguation

interface Place : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val address: String
    val type: String?
    val coordinates: Coordinates?
    val lifeSpan: LifeSpan?
}
