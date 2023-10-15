package ly.david.musicsearch.core.models.event

import ly.david.musicsearch.core.models.LifeSpan
import ly.david.musicsearch.core.models.NameWithDisambiguation

interface Event : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val time: String?
    val cancelled: Boolean?
    val lifeSpan: LifeSpan?
}
