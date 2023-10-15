package ly.david.musicsearch.data.core.series

import ly.david.musicsearch.data.core.listitem.RelationListItemModel

data class SeriesScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Series
