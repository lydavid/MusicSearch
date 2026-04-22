package ly.david.musicsearch.shared.domain.list

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toPersistentSet
import ly.david.musicsearch.shared.domain.recording.RecordingSortOption
import ly.david.musicsearch.shared.domain.release.ReleaseSortOption
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupSortOption

// TODO: rename this to SortFilters
sealed interface SortOption {
    data object None : SortOption

    data class Recording(
        val option: RecordingSortOption = RecordingSortOption.InsertedAscending,
    ) : SortOption

    data class Release(
        val option: ReleaseSortOption = ReleaseSortOption.InsertedAscending,
        val showMoreInfo: Boolean = true,
        val showStatuses: ImmutableSet<ReleaseStatus> = ReleaseStatus.entries.toPersistentSet(),
    ) : SortOption

    data class ReleaseGroup(
        val option: ReleaseGroupSortOption = ReleaseGroupSortOption.InsertedAscending,
    ) : SortOption
}

fun SortOption.showTypes(): Boolean {
    val sortOption = (this as? SortOption.ReleaseGroup)?.option ?: return false
    return !setOf(
        ReleaseGroupSortOption.PrimaryTypeAscending,
        ReleaseGroupSortOption.PrimaryTypeDescending,
    ).contains(sortOption)
}
