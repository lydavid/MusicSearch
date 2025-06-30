package ly.david.musicsearch.shared.domain.listitem

/**
 * Content that appears in front of a list of [ListItemModel].
 * There should only be at most one header per list.
 */
data object Header : ListItemModel {
    override val id: String = "Header"
}
