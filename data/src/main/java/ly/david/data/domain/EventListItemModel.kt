package ly.david.data.domain

import ly.david.data.Event
import ly.david.data.LifeSpan
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.persistence.event.EventRoomModel

data class EventListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val lifeSpan: LifeSpan? = null,
) : Event, ListItemModel()

fun EventMusicBrainzModel.toEventListItemModel() =
    EventListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = lifeSpan
    )

fun EventRoomModel.toEventListItemModel() =
    EventListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = lifeSpan
    )
