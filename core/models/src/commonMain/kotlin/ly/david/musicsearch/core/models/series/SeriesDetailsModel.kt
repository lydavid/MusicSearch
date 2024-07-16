package ly.david.musicsearch.core.models.series

import ly.david.musicsearch.core.models.listitem.RelationListItemModel

data class SeriesDetailsModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Series
