package ly.david.musicsearch.shared.domain.sort

@Suppress("MagicNumber")
enum class InstrumentSortOption(val order: Int) : SortableOption {
    InsertedAscending(0),
    InsertedDescending(1),
    NameAscending(2),
    NameDescending(3),
}
