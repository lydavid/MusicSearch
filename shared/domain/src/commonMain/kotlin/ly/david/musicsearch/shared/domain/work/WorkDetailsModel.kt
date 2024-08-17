package ly.david.musicsearch.shared.domain.work

import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel

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
