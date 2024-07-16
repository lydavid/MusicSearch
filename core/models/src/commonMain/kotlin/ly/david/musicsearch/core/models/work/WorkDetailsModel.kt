package ly.david.musicsearch.core.models.work

import ly.david.musicsearch.core.models.listitem.RelationListItemModel

data class WorkDetailsModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val language: String? = null,
    override val iswcs: List<String>? = null,
    val attributes: List<WorkAttributeUiModel> = listOf(),
    val urls: List<RelationListItemModel> = listOf(),
) : Work
