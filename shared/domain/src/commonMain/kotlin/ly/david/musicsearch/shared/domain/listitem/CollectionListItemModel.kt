package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

/**
 * @param cachedEntityCount The number of entities in this collection in our local database.
 */
data class CollectionListItemModel(
    override val id: String,
    val isRemote: Boolean,
    val name: String,
    val description: String = "",
    val entity: MusicBrainzEntityType,
    val cachedEntityCount: Int = 0,
    override val visited: Boolean = false,
    val containsEntity: Boolean = false,
) : ListItemModel, Visitable
