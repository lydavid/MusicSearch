package ly.david.data.domain

import ly.david.data.Label
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.persistence.label.LabelRoomModel
import ly.david.data.persistence.release.LabelWithCatalog

data class LabelCardModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,

    val catalogNumber: String? = null
) : Label, UiModel()

internal fun LabelMusicBrainzModel.toLabelUiModel() =
    LabelCardModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode
    )

internal fun LabelRoomModel.toLabelUiModel() =
    LabelCardModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode
    )

internal fun LabelWithCatalog.toCardModel() =
    LabelCardModel(
        id = label.id,
        name = label.name,
        disambiguation = label.disambiguation,
        type = label.type,
        labelCode = label.labelCode,
        catalogNumber = releaseLabel.catalogNumber
    )
