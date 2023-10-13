package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.ArtistMusicBrainzModel
import ly.david.musicsearch.data.core.listitem.ArtistListItemModel
import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.domain.common.toLifeSpanUiModel
import lydavidmusicsearchdatadatabase.Artist

fun ArtistMusicBrainzModel.toArtistListItemModel() =
    ArtistListItemModel(
        id = id,
        name = name,
        sortName = sortName,
        disambiguation = disambiguation,
        type = type,
        gender = gender,
        countryCode = countryCode,
        lifeSpan = lifeSpan?.toLifeSpanUiModel()
    )


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
