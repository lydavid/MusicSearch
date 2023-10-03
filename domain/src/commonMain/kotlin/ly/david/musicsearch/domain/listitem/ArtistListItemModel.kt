package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.ArtistMusicBrainzModel
import ly.david.musicsearch.domain.common.LifeSpanUiModel
import ly.david.musicsearch.domain.common.toLifeSpanUiModel
import lydavidmusicsearchdatadatabase.Artist

data class ArtistListItemModel(
    override val id: String,
    override val name: String,
    override val sortName: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val gender: String? = null,
    override val countryCode: String? = null,
    val lifeSpan: LifeSpanUiModel? = null,
) : ListItemModel(), ly.david.data.core.artist.Artist

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
