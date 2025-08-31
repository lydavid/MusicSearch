package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.image.ImageId
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
    imageUrl = thumbnailUrl,
    imageId = imageId?.let { ImageId(it) },
    visited = visited == true,
    collected = collected == true,
    aliases = combineToAliases(aliasNames, aliasLocales),
)
