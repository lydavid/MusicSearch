package ly.david.musicsearch.domain.artist

import ly.david.data.core.artist.Artist
import ly.david.data.core.artist.ArtistForDetails
import ly.david.musicsearch.domain.common.LifeSpanUiModel
import ly.david.musicsearch.domain.listitem.RelationListItemModel
import ly.david.musicsearch.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Relation

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
    urls: List<Relation>,
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
    urls = urls.map { it.toRelationListItemModel() }
)
