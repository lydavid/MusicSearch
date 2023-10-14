package ly.david.musicsearch.data.core.event

import ly.david.musicsearch.data.core.LifeSpan
import ly.david.musicsearch.data.core.NameWithDisambiguation

interface Event : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val time: String?
    val cancelled: Boolean?
    val lifeSpan: LifeSpan?
}
