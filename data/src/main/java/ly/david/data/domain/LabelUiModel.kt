package ly.david.data.domain

import ly.david.data.Label
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.persistence.label.LabelRoomModel

data class LabelUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,

    // TODO: catalogue: this normally belongs to a release/label, but we would like to display them
    //  on a label card when viewing a release
) : Label, UiModel()

internal fun LabelMusicBrainzModel.toLabelUiModel() =
    LabelUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode
    )

internal fun LabelRoomModel.toLabelUiModel() =
    LabelUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode
    )
