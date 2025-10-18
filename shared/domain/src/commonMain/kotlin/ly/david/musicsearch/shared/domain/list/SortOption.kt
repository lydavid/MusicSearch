package ly.david.musicsearch.shared.domain.list

import ly.david.musicsearch.shared.domain.recording.RecordingSortOption

sealed interface SortOption {
    data object None : SortOption

    data class Recording(
        val option: RecordingSortOption = RecordingSortOption.None,
    ) : SortOption

    data class Release(
        val sorted: Boolean = false,
        val showMoreInfo: Boolean = true,
    ) : SortOption

    data class ReleaseGroup(
        val sorted: Boolean = false,
    ) : SortOption
}
