package ly.david.musicsearch.data.core.work

import ly.david.musicsearch.data.core.Work
import ly.david.musicsearch.data.core.listitem.RelationListItemModel

data class WorkScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val language: String? = null,
    override val iswcs: List<String>? = null,
    val attributes: List<WorkAttributeUiModel> = listOf(),
    val urls: List<RelationListItemModel> = listOf(),
) : Work
