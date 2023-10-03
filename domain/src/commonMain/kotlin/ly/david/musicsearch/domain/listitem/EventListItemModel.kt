package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.EventMusicBrainzModel
import ly.david.musicsearch.domain.common.LifeSpanUiModel
import ly.david.musicsearch.domain.common.toLifeSpanUiModel
import lydavidmusicsearchdatadatabase.Event

data class EventListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val time: String? = null,
    override val cancelled: Boolean? = null,
    override val lifeSpan: LifeSpanUiModel? = null,
) : ly.david.data.core.Event, ListItemModel()

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

fun Event.toEventListItemModel() =
    EventListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        time = time,
        cancelled = cancelled,
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
    )
