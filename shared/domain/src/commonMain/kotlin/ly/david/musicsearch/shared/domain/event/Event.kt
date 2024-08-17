package ly.david.musicsearch.shared.domain.event

import ly.david.musicsearch.shared.domain.LifeSpan
import ly.david.musicsearch.shared.domain.NameWithDisambiguation

interface Event : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val time: String?
    val cancelled: Boolean?
    val lifeSpan: LifeSpan?
}
