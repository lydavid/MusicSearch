package ly.david.musicsearch.core.models.listitem

import ly.david.musicsearch.core.models.genre.Genre

data class GenreListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
) : Genre, ListItemModel()
