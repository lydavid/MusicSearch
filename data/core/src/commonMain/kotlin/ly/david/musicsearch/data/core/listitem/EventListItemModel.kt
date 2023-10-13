package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.Event
import ly.david.musicsearch.data.core.LifeSpanUiModel

data class EventListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val time: String? = null,
    override val cancelled: Boolean? = null,
    override val lifeSpan: LifeSpanUiModel? = null,
) : Event, ListItemModel()
