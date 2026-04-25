package ly.david.musicsearch.shared.domain.list

// order matters
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
