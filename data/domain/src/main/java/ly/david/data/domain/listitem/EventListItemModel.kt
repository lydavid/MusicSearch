package ly.david.data.domain.listitem

import ly.david.data.Event
import ly.david.data.LifeSpanUiModel
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.room.event.EventRoomModel
import ly.david.data.toLifeSpanUiModel

data class EventListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val time: String? = null,
    override val cancelled: Boolean? = null,
    override val lifeSpan: LifeSpanUiModel? = null,
) : Event, ListItemModel()

fun EventMusicBrainzModel.toEventListItemModel() =
    EventListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        time = time,
        cancelled = cancelled,
        lifeSpan = lifeSpan?.toLifeSpanUiModel()
    )

fun EventRoomModel.toEventListItemModel() =
    EventListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        time = time,
        cancelled = cancelled,
        lifeSpan = lifeSpan?.toLifeSpanUiModel()
    )
