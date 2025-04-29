package ly.david.musicsearch.shared.domain.listitem

/**
 * Represents the end of the list of [ListItemModel] being displayed.
 * There should only be at most one footer per list.
 */
data class Footer(
    override val id: String = "Footer",
    val text: String = "",
) : ListItemModel()
