package ly.david.data.domain.work

import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Relation
import lydavidmusicsearchdatadatabase.Work
import lydavidmusicsearchdatadatabase.Work_attribute

data class WorkScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val language: String? = null,
    override val iswcs: List<String>? = null,
    val attributes: List<WorkAttributeUiModel> = listOf(),
    val urls: List<RelationListItemModel> = listOf(),
) : ly.david.data.core.Work

internal fun Work.toWorkScaffoldModel(
    workAttributes: List<Work_attribute>,
    urls: List<Relation>,
) = WorkScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    language = language,
    iswcs = iswcs,
    attributes = workAttributes.map { it.toWorkAttributeUiModel() },
    urls = urls.map { it.toRelationListItemModel() },
)
