package ly.david.musicsearch.core.models.listitem

import ly.david.musicsearch.core.models.event.Event
import ly.david.musicsearch.core.models.LifeSpanUiModel

data class EventListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val time: String? = null,
    override val cancelled: Boolean? = null,
    override val lifeSpan: LifeSpanUiModel? = null,
) : Event, ListItemModel()
