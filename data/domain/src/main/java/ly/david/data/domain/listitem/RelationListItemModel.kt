package ly.david.data.domain.listitem

import ly.david.data.Relation
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.relation.RelationRoomModel

/**
 * @param id For reordering animation in a lazy list.
 *  This must have unique among all [RelationListItemModel] that may appear in the same lazy list.
 *  The most obvious choice is the primary keys from [RelationRoomModel].
 *  Since these are meant to be displayed inside a tab for a given resource with the id [RelationRoomModel.entityId],
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

fun RelationRoomModel.toRelationListItemModel() =
    RelationListItemModel(
        id = "${linkedEntityId}_$order",
        linkedEntityId = linkedEntityId,
        linkedEntity = linkedEntity,
        label = label,
        name = name,
        disambiguation = disambiguation,
        attributes = attributes,
        additionalInfo = additionalInfo
    )
