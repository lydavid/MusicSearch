package ly.david.musicsearch.shared.domain.genre

import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel

interface GenreRepository {
    suspend fun lookupGenre(
        id: String,
        forceRefresh: Boolean,
    ): GenreListItemModel
}
