package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Relation
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.RelationRoomModel

internal data class RelationUiModel(
    override val linkedResourceId: String,
    override val label: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val attributes: String? = null,
    override val additionalInfo: String? = null,
    override val linkedResource: MusicBrainzResource,
) : UiModel(), Relation

internal fun RelationRoomModel.toRelationUiModel() =
    RelationUiModel(
        linkedResourceId = linkedResourceId,
        linkedResource = linkedResource,
        label = label,
        name = name,
        disambiguation = disambiguation,
        attributes = attributes,
        additionalInfo = additionalInfo
    )
