package ly.david.musicsearch.shared.domain.sort

@Suppress("MagicNumber")
enum class PlaceSortOption(val order: Int) : SortableOption {
    InsertedAscending(0),
    InsertedDescending(1),
    NameAscending(2),
    NameDescending(3),
    AddressAscending(4),
    AddressDescending(5),
    DateAscending(6),
    DateDescending(7),
}
