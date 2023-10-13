package ly.david.musicsearch.domain.listitem

import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import lydavidmusicsearchdatadatabase.Relation

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
