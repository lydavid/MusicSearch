package ly.david.musicsearch.shared.domain.sort

@Suppress("MagicNumber")
enum class ArtistSortOption(val order: Int) : SortableOption {
    InsertedAscending(0),
    InsertedDescending(1),
    NameAscending(2),
    NameDescending(3),
    DateAscending(4),
    DateDescending(5),
}
