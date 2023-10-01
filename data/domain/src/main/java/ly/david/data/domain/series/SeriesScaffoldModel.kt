package ly.david.data.domain.series

import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Relation
import lydavidmusicsearchdatadatabase.Series

data class SeriesScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ly.david.data.core.Series

internal fun Series.toSeriesScaffoldModel(
    urls: List<Relation>,
) = SeriesScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    urls = urls.map { it.toRelationListItemModel() },
)
