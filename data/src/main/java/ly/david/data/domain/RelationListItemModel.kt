package ly.david.data.domain

import ly.david.data.Relation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.relation.RelationRoomModel

data class RelationListItemModel(
    override val linkedResourceId: String,
    override val label: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val attributes: String? = null,
    override val additionalInfo: String? = null,
    override val linkedResource: MusicBrainzResource,
) : ListItemModel(), Relation

fun RelationRoomModel.toRelationListItemModel() =
    RelationListItemModel(
        linkedResourceId = linkedResourceId,
        linkedResource = linkedResource,
        label = label,
        name = name,
        disambiguation = disambiguation,
        attributes = attributes,
        additionalInfo = additionalInfo
    )
