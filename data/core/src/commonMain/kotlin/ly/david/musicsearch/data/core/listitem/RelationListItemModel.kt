package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.Relation
import ly.david.musicsearch.data.core.network.MusicBrainzEntity

/**
 * @param id For reordering animation in a lazy list.
 *  This must be unique among all [RelationListItemModel] that may appear in the same lazy list.
 *  The most obvious choice is the primary keys from its database table.
 *  Since these are meant to be displayed inside a tab for a given resource with the id [Relation.entity_id],
 *  we don't have to include that.
 */
data class RelationListItemModel(
    override val id: String,
    override val linkedEntityId: String,
    override val label: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val attributes: String? = null,
    override val additionalInfo: String? = null,
    override val linkedEntity: MusicBrainzEntity,
) : ListItemModel(), Relation

data class RelationWithOrder(
    override val id: String,
    override val linkedEntityId: String,
    override val label: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val attributes: String? = null,
    override val additionalInfo: String? = null,
    override val linkedEntity: MusicBrainzEntity,
    val order: Int,
) : ListItemModel(), Relation
