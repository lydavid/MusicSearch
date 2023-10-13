package ly.david.musicsearch.domain.series

import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import lydavidmusicsearchdatadatabase.Series

data class SeriesScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ly.david.musicsearch.data.core.Series

internal fun Series.toSeriesScaffoldModel(
    urls: List<RelationListItemModel>,
) = SeriesScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    urls = urls,
)
