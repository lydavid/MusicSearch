package ly.david.data.domain.work

import ly.david.data.Work
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import ly.david.data.room.work.WorkWithAllData

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

internal fun WorkWithAllData.toWorkScaffoldModel() =
    WorkScaffoldModel(
        id = work.id,
        name = work.name,
        disambiguation = work.disambiguation,
        type = work.type,
        language = work.language,
        iswcs = work.iswcs,
        attributes = attributes.map { it.toWorkAttributeUiModel() },
        urls = urls.map { it.relation.toRelationListItemModel() },
    )
