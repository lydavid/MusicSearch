package ly.david.musicsearch.shared.domain.sort

@Suppress("MagicNumber")
enum class RecordingSortOption(val order: Int) : SortableOption {
    InsertedAscending(0),
    InsertedDescending(1),
    NameAscending(2),
    NameDescending(3),
    DateAscending(4),
    DateDescending(5),
    ListensAscending(6),
    ListensDescending(7),
}
