package ly.david.musicsearch.shared.domain.listitem

/**
 * Represents the end of the list of [ListItemModel] being displayed.
 */
data object EndOfList : ListItemModel() {
    override val id: String = "EndOfList"
}
