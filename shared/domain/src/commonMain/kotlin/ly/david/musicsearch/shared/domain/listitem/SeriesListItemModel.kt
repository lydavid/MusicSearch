package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.series.Series

data class SeriesListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val visited: Boolean = false,
) : ListItemModel(), Series, Visitable
