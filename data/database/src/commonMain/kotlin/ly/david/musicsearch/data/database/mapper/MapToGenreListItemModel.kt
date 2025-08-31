package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel

fun mapToGenreListItemModel(
    id: String,
    name: String,
    disambiguation: String,
    visited: Boolean?,
    collected: Boolean?,
) = GenreListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    visited = visited == true,
    collected = collected == true,
)
