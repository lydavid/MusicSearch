package ly.david.musicsearch.shared.domain.sort

@Suppress("MagicNumber")
enum class WorkSortOption(val order: Int) : SortableOption {
    InsertedAscending(0),
    InsertedDescending(1),
    NameAscending(2),
    NameDescending(3),
    ListensAscending(4),
    ListensDescending(5),
}
