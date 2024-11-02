package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.Relation

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
    override val visited: Boolean = false,
) : ListItemModel(), Relation, Visitable
