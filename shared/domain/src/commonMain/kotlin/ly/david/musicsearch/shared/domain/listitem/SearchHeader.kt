package ly.david.musicsearch.shared.domain.listitem

class SearchHeader(
    override val id: String = "SearchHeader",
    val remoteCount: Int,
) : ListItemModel()
