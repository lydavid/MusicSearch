package ly.david.data.domain.listitem

import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.room.relation.RelationRoomModel
import lydavidmusicsearchdatadatabase.Relation

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
) : ListItemModel(), ly.david.data.core.Relation

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

fun Relation.toRelationListItemModel() =
    RelationListItemModel(
        id = "${linked_entity_id}_$order",
        linkedEntityId = linked_entity_id,
        linkedEntity = linked_entity,
        label = label,
        name = name,
        disambiguation = disambiguation,
        attributes = attributes,
        additionalInfo = additional_info
    )
