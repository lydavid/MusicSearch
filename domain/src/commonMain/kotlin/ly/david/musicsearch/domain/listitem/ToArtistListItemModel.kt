package ly.david.musicsearch.domain.listitem

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.ArtistListItemModel
import lydavidmusicsearchdatadatabase.Artist

fun Artist.toArtistListItemModel() =
    ArtistListItemModel(
        id = id,
        name = name,
        sortName = sort_name,
        disambiguation = disambiguation,
        type = type,
        gender = gender,
        countryCode = country_code,
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        )
    )
