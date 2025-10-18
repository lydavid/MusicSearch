package ly.david.musicsearch.shared.domain

import ly.david.musicsearch.shared.domain.list.SortOption

data class ListFilters(
    val query: String = "",
    val isRemote: Boolean = true,
    val sorted: Boolean = false,
    val username: String = "",
    val sortOption: SortOption = SortOption.None,
)
