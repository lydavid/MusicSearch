package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.Identifiable

/**
 * Data that can be displayed in lists should extend this.
 */
sealed class ListItemModel : Identifiable

/**
 * Content that appears in front of a list of [ListItemModel].
 * There should only be at most one header per list.
 */
class Header(
    override val id: String = "Header",
) : ListItemModel()

/**
 * Represents the end of the list of [ListItemModel] being displayed.
 */
data object EndOfList : ListItemModel() {
    override val id: String = "EndOfList"
}

/**
 * Represents a separator with [text] that can be inserted between two list items.
 */
class ListSeparator(
    override val id: String,
    val text: String,
) : ListItemModel()
