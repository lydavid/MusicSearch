package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.series.Series

data class SeriesListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
) : Series, ListItemModel()
