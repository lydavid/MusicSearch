package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.EventMusicBrainzModel
import ly.david.musicsearch.data.core.listitem.EventListItemModel
import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.domain.common.toLifeSpanUiModel
import lydavidmusicsearchdatadatabase.Event

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
