package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Relation
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.recording.RelationRoomModel

internal data class RelationUiModel(
    override val resourceId: String, // todo not needed
    override val resource: MusicBrainzResource,
    override val linkedResourceId: String,
    override val order: Int, // TODO: not needed? only for the room model so that we get them in order
    override val label: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val attributes: String? = null,
    override val additionalInfo: String? = null,
    override val linkedResource: MusicBrainzResource,
) : UiModel(), Relation

internal fun RelationRoomModel.toRelationUiModel() =
    RelationUiModel(
        resourceId = resourceId,
        resource = resource,
        linkedResourceId = linkedResourceId,
        linkedResource = linkedResource,
        order = order,
        label = label,
        name = name,
        disambiguation = disambiguation,
        attributes = attributes,
        additionalInfo = additionalInfo
    )
