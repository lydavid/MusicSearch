package ly.david.musicsearch.shared.domain.listitem

/**
 * Represents a separator with [text] that can be inserted between two list items.
 */
data class ListSeparator(
    override val id: String,
    val text: String,
) : ListItemModel()
