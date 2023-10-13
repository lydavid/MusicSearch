package ly.david.musicsearch.domain.series

import ly.david.musicsearch.domain.listitem.RelationListItemModel
import ly.david.musicsearch.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Relation
import lydavidmusicsearchdatadatabase.Series

data class SeriesScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ly.david.musicsearch.data.core.Series

internal fun Series.toSeriesScaffoldModel(
    urls: List<Relation>,
) = SeriesScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    urls = urls.map { it.toRelationListItemModel() },
)
