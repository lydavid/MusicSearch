package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel

data class ArtistDetailsModel(
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
