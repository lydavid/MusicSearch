package ly.david.musicsearch.core.models

data class ListFilters(
    val query: String = "",
    val isRemote: Boolean = true,
    val sorted: Boolean = false,
)
