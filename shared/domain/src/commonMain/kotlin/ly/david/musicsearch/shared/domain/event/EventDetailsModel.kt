package ly.david.musicsearch.shared.domain.event

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel

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
