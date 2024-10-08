package ly.david.musicsearch.shared.domain.place

import ly.david.musicsearch.shared.domain.LifeSpan
import ly.david.musicsearch.shared.domain.NameWithDisambiguation

interface Place : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val address: String
    val type: String?
    val coordinates: Coordinates?
    val lifeSpan: LifeSpan?
}
