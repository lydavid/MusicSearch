package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.genre.Genre

data class GenreListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val visited: Boolean = false,
    override val collected: Boolean = false,
) : EntityListItemModel, Genre
