package ly.david.musicsearch.shared.domain.releasegroup

import ly.david.musicsearch.shared.domain.list.SortableOption

enum class ReleaseGroupSortOption : SortableOption {
    InsertedAscending,
    InsertedDescending,
    NameAscending,
    NameDescending,
    DateAscending,
    DateDescending,
    PrimaryTypeAscending,
    PrimaryTypeDescending,
}
