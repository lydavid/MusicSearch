package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.AreaMusicBrainzModel
import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.AreaListItemModel
import ly.david.musicsearch.domain.common.toLifeSpanUiModel
import lydavidmusicsearchdatadatabase.Area

fun AreaMusicBrainzModel.toAreaListItemModel(date: String? = null) = AreaListItemModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = lifeSpan?.toLifeSpanUiModel(),
    countryCodes = countryCodes,
    date = date
)

fun Area.toAreaListItemModel() = AreaListItemModel(
    id = id,
    name = name,
    sortName = sort_name,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = LifeSpanUiModel(
        begin = begin,
        end = end,
        ended = ended,
    ),
)
