package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.collection.PendingEdit
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

/**
 * @param cachedEntityCount The number of entities in this collection in our local database.
 * @param remoteEntityCount The number of entities in this collection in MusicBrainz.
 * If we don't know yet, it will be `null`.
 * @param containsEntities Whether this collection contains some, all, or none of the entities being edited.
 * @param pendingEdit Pending edit state that takes precedence over [containsEntities].
 *  It must be separately tracked so that we know the original state.
 */
data class CollectionListItemModel(
    override val id: String,
    val isRemote: Boolean,
    val name: String,
    val description: String = "",
    val entity: MusicBrainzEntityType,
    val cachedEntityCount: Int = 0,
    val remoteEntityCount: Int? = null,
    override val visited: Boolean = false,
    val containsEntities: CollectionContainsEntities? = null,
    val pendingEdit: PendingEdit? = null,
) : ListItemModel, Visitable

enum class CollectionContainsEntities {
    None,
    Some,
    All,
}
