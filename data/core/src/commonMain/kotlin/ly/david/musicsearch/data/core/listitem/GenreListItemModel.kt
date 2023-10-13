package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.Genre

data class GenreListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
) : Genre, ListItemModel()
