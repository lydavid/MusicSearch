package ly.david.musicsearch.core.models.listitem

import ly.david.musicsearch.core.models.series.Series

data class SeriesListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
) : Series, ListItemModel()
