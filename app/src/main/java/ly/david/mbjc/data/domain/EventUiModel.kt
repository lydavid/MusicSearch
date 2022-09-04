package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Event
import ly.david.mbjc.data.LifeSpan
import ly.david.mbjc.data.network.EventMusicBrainzModel

internal data class EventUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val lifeSpan: LifeSpan? = null,

    // TODO: display relations that come with search API (artists/location)
    //  perhaps don't display too much data in search card. These data can be found after clicking into it
    //  Mobile experience doesn't have to be 1-to-1 with web.
) : Event, UiModel()

internal fun EventMusicBrainzModel.toEventUiModel() =
    EventUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = lifeSpan
    )
