package ly.david.musicsearch.shared.domain

import ly.david.musicsearch.shared.domain.recording.RecordingSortOption

data class ListFilters(
    val query: String = "",
    val isRemote: Boolean = true,
    val sorted: Boolean = false,
    val username: String = "",
    val recordingSortOption: RecordingSortOption = RecordingSortOption.None,
)
