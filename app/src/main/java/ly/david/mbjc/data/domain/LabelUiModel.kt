package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Label
import ly.david.mbjc.data.network.LabelMusicBrainzModel

internal data class LabelUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,
) : Label, UiModel()

internal fun LabelMusicBrainzModel.toLabelUiModel() =
    LabelUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode
    )
