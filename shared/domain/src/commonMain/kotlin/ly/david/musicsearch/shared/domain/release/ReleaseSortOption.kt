package ly.david.musicsearch.shared.domain.release

import ly.david.musicsearch.shared.domain.list.SortableOption

enum class ReleaseSortOption : SortableOption {
    InsertedAscending,
    InsertedDescending,
    NameAscending,
    NameDescending,
    DateAscending,
    DateDescending,
    ListensAscending,
    ListensDescending,
    CompleteListensAscending,
    CompleteListensDescending,
}
