package ly.david.data.domain.work

import ly.david.data.WorkAttribute
import ly.david.data.network.WorkAttributeMusicBrainzModel
import ly.david.data.room.work.WorkAttributeRoomModel

data class WorkAttributeUiModel(
    override val type: String,
    override val typeId: String,
    override val value: String
) : WorkAttribute

internal fun WorkAttributeMusicBrainzModel.toWorkAttributeUiModel() =
    WorkAttributeUiModel(
        type = type,
        typeId = typeId,
        value = value
    )

internal fun WorkAttributeRoomModel.toWorkAttributeUiModel() =
    WorkAttributeUiModel(
        type = type,
        typeId = typeId,
        value = value
    )
