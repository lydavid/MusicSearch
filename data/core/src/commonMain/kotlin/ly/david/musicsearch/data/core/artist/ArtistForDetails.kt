package ly.david.musicsearch.data.core.artist

import ly.david.musicsearch.data.core.LifeSpan
import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.RelationListItemModel

data class ArtistForDetails(
    override val id: String,
    override val name: String,
    override val sortName: String,
    override val disambiguation: String?,
    override val type: String?,
    override val gender: String?,
    override val countryCode: String?,
    override val begin: String?,
    override val end: String?,
    override val ended: Boolean?,
    val imageUrl: String?,
) : Artist, LifeSpan

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
