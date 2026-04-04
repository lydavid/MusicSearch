package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.image.ImageSource
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel

fun mapToEventListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    type: String,
    time: String,
    cancelled: Boolean,
    begin: String,
    end: String,
    ended: Boolean,
    thumbnailUrl: String?,
    imageId: Long?,
    source: ImageSource?,
    visited: Boolean?,
    collected: Boolean?,
    aliasNames: String?,
    aliasLocales: String?,
) = EventListItemModel(
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
    imageMetadata = mapToImageMetadata(
        id = imageId,
        thumbnailUrl = thumbnailUrl,
        source = source,
    ),
    visited = visited == true,
    collected = collected == true,
    aliases = combineToAliases(aliasNames, aliasLocales),
)
