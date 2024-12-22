package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

data class ArtistDetailsModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val gender: String? = null,
    val ipis: List<String>? = null,
    val isnis: List<String>? = null,
    val lifeSpan: LifeSpanUiModel = LifeSpanUiModel(),
    val urls: List<RelationListItemModel> = listOf(),
    val imageUrl: String? = null,
    val placeholderKey: Long? = null,
    val wikipediaExtract: WikipediaExtract = WikipediaExtract(),
    val areaListItemModel: AreaListItemModel? = null,
) : Artist
