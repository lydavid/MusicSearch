package ly.david.musicsearch.shared.domain.listitem

/**
 * Content that appears in front of a list of [ListItemModel].
 * There should only be at most one header per list.
 */
class Header(
    override val id: String = "Header",
) : ListItemModel()

class SearchHeader(
    override val id: String = "SearchHeader",
    val remoteCount: Int,
) : ListItemModel()
