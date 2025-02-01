package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel

fun mapToArtistListItemModel(
    id: String,
    name: String,
    sortName: String,
    disambiguation: String?,
    type: String?,
    gender: String?,
    countryCode: String?,
    begin: String?,
    end: String?,
    ended: Boolean?,
    thumbnailUrl: String?,
    visited: Boolean?,
) = ArtistListItemModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = type,
    gender = gender,
    countryCode = countryCode,
    lifeSpan = LifeSpanUiModel(
        begin = begin,
        end = end,
        ended = ended,
    ),
    imageUrl = thumbnailUrl,
    visited = visited == true,
)
