package ly.david.data.domain.series

import ly.david.data.Series
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import ly.david.data.room.series.SeriesWithAllData

data class SeriesScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Series

internal fun SeriesWithAllData.toSeriesScaffoldModel() =
    SeriesScaffoldModel(
        id = series.id,
        name = series.name,
        disambiguation = series.disambiguation,
        type = series.type,
        urls = urls.map { it.relation.toRelationListItemModel() },
    )
