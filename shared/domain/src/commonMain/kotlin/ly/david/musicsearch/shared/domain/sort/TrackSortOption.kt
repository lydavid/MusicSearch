package ly.david.musicsearch.shared.domain.sort

@Suppress("MagicNumber")
enum class TrackSortOption(val order: Int) : SortableOption {
    NormalAscending(0),
    NormalDescending(1),
    MostListenCountDescending(2),
    MostListenCountAscending(3),
    MostLastListenedAtDescending(4),
    MostLastListenedAtAscending(5),
}
