package ly.david.musicsearch.shared.domain.sort

@Suppress("MagicNumber")
enum class ReleaseSortOption(val order: Int) : SortableOption {
    InsertedAscending(0),
    InsertedDescending(1),
    NameAscending(2),
    NameDescending(3),
    DateAscending(4),
    DateDescending(5),
    ListensAscending(6),
    ListensDescending(7),
    CompleteListensAscending(8),
    CompleteListensDescending(9),
}
