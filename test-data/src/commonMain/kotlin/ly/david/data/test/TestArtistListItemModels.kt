package ly.david.data.test

import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistType
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel

val variousArtistsArtistListItemModel = ArtistListItemModel(
    id = "89ad4ac3-39f7-470e-963a-56509c546377",
    name = "Various Artists",
    sortName = "Various Artists",
    disambiguation = "add compilations to this artist",
    type = ArtistType.Other,
)

val roseliaArtistListItemModel = ArtistListItemModel(
    id = "adea3c3d-a84d-4f9e-ac0b-1ef71a8947a5",
    name = "Roselia",
    sortName = "Roselia",
    type = ArtistType.Group,
    disambiguation = "BanG Dream!",
    lifeSpan = LifeSpanUiModel(
        begin = "2016-09-15",
    ),
)

val itouKanakoArtistListItemModel = ArtistListItemModel(
    id = "eee65fbc-ead0-4c01-b385-a6f563c418d3",
    name = "いとうかなこ",
    sortName = "Ito, Kanako",
    type = ArtistType.Person,
    disambiguation = "singer",
    lifeSpan = LifeSpanUiModel(
        begin = "1973-03-28",
    ),
    aliases = persistentListOf(
        BasicAlias(
            name = "Kanako Ito",
            locale = "en",
            isPrimary = true,
        ),
        BasicAlias(
            name = "いとうかなこ",
            locale = "ja",
            isPrimary = true,
        ),
    ),
)
