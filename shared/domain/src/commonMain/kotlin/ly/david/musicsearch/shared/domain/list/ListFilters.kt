package ly.david.musicsearch.shared.domain.list

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toPersistentSet
import ly.david.musicsearch.shared.domain.release.ReleaseStatus

sealed interface ListFilters {
    val query: String
    val isRemote: Boolean

    data class Base(
        override val query: String = "",
        override val isRemote: Boolean = true,
    ) : ListFilters

    data class Artists(
        override val query: String = "",
        override val isRemote: Boolean = true,
        val sortOption: ArtistSortOption = ArtistSortOption.InsertedAscending,
    ) : ListFilters

    data class Recordings(
        override val query: String = "",
        override val isRemote: Boolean = true,
        val username: String = "",
        val sortOption: RecordingSortOption = RecordingSortOption.InsertedAscending,
    ) : ListFilters

    data class Releases(
        override val query: String = "",
        override val isRemote: Boolean = true,
        val username: String = "",
        val sortOption: ReleaseSortOption = ReleaseSortOption.InsertedAscending,
        val showMoreInfo: Boolean = true,
        val showStatuses: ImmutableSet<ReleaseStatus> = ReleaseStatus.entries.toPersistentSet(),
    ) : ListFilters

    data class ReleaseGroups(
        override val query: String = "",
        override val isRemote: Boolean = true,
        val sortOption: ReleaseGroupSortOption = ReleaseGroupSortOption.InsertedAscending,
    ) : ListFilters

    data class Works(
        override val query: String = "",
        override val isRemote: Boolean = true,
        val username: String = "",
    ) : ListFilters
}

fun ListFilters.showTypes(): Boolean {
    val sortOption = (this as ListFilters.ReleaseGroups).sortOption
    return !setOf(
        ReleaseGroupSortOption.PrimaryTypeAscending,
        ReleaseGroupSortOption.PrimaryTypeDescending,
    ).contains(sortOption)
}
