package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.artist.ArtistGender
import ly.david.musicsearch.shared.domain.artist.ArtistType
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel

fun mapToArtistListItemModel(
    id: String,
    name: String,
    sortName: String,
    disambiguation: String,
    typeId: String,
    gender: String,
    countryCode: String,
    begin: String,
    end: String,
    ended: Boolean,
    thumbnailUrl: String?,
    imageId: Long?,
    visited: Boolean?,
    collected: Boolean?,
    aliasNames: String?,
    aliasLocales: String?,
) = ArtistListItemModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = ArtistType.fromId(typeId),
    gender = ArtistGender.fromId(gender),
    countryCode = countryCode,
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
