package ly.david.musicsearch.core.models.event

import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.listitem.RelationListItemModel

data class EventDetailsModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val time: String? = null,
    override val cancelled: Boolean? = null,
    override val lifeSpan: LifeSpanUiModel? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Event
