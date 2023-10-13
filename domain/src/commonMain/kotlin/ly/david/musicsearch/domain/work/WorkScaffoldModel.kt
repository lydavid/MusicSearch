package ly.david.musicsearch.domain.work

import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import ly.david.musicsearch.data.core.work.WorkAttributeUiModel
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
) : ly.david.musicsearch.data.core.Work

internal fun Work.toWorkScaffoldModel(
    workAttributes: List<Work_attribute>,
    urls: List<RelationListItemModel>,
) = WorkScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    language = language,
    iswcs = iswcs,
    attributes = workAttributes.map { it.toWorkAttributeUiModel() },
    urls = urls,
)
