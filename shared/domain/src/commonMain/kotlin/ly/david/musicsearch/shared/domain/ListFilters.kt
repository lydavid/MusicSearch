package ly.david.musicsearch.shared.domain

data class ListFilters(
    val query: String = "",
    val isRemote: Boolean = true,
    val sorted: Boolean = false,
    val username: String = "",
)
