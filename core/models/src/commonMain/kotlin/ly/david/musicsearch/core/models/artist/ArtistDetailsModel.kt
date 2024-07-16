package ly.david.musicsearch.core.models.artist

import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.listitem.RelationListItemModel

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
