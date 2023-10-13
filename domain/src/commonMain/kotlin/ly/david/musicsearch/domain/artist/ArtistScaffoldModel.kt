package ly.david.musicsearch.domain.artist

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.artist.Artist
import ly.david.musicsearch.data.core.artist.ArtistForDetails
import ly.david.musicsearch.data.core.listitem.RelationListItemModel

data class ArtistScaffoldModel(
    override val id: String,
    override val name: String,
    override val sortName: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val gender: String? = null,
    override val countryCode: String? = null,
    val lifeSpan: LifeSpanUiModel? = null,
    val imageUrl: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Artist

internal fun ArtistForDetails.toArtistScaffoldModel(
    urls: List<RelationListItemModel>,
) = ArtistScaffoldModel(
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
    imageUrl = imageUrl,
    urls = urls,
)
