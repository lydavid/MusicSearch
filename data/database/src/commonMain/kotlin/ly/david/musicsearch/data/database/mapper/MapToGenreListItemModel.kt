package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.core.models.listitem.GenreListItemModel

fun mapToGenreListItemModel(
    id: String,
    name: String,
    disambiguation: String?,
) = GenreListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
)
