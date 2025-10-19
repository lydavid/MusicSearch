package ly.david.musicsearch.shared.domain.list

import ly.david.musicsearch.shared.domain.recording.RecordingSortOption
import ly.david.musicsearch.shared.domain.release.ReleaseSortOption

sealed interface SortOption {
    data object None : SortOption

    data class Recording(
        val option: RecordingSortOption = RecordingSortOption.InsertedAscending,
    ) : SortOption

    data class Release(
        val option: ReleaseSortOption = ReleaseSortOption.InsertedAscending,
        val showMoreInfo: Boolean = true,
    ) : SortOption

    data class ReleaseGroup(
        val sorted: Boolean = false,
    ) : SortOption
}
