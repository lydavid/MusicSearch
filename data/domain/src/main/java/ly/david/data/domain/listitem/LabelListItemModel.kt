package ly.david.data.domain.listitem

import ly.david.data.core.Label
import ly.david.data.network.LabelInfo
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.room.label.LabelRoomModel
import ly.david.data.room.release.LabelWithCatalog

data class LabelListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,

    // TODO: don't need this in scaffold model
    val catalogNumber: String? = null,
) : Label, ListItemModel()

internal fun LabelMusicBrainzModel.toLabelListItemModel() =
    LabelListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode
    )

fun LabelRoomModel.toLabelListItemModel() =
    LabelListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode
    )

internal fun LabelWithCatalog.toLabelListItemModel() =
    LabelListItemModel(
        id = label.id,
        name = label.name,
        disambiguation = label.disambiguation,
        type = label.type,
        labelCode = label.labelCode,
        catalogNumber = releaseLabel.catalogNumber
    )

fun List<LabelInfo>.toLabelListItemModels(): List<LabelListItemModel> {
    return this.mapNotNull { labelInfo ->
        val label = labelInfo.label
        if (label == null) {
            null
        } else {
            LabelListItemModel(
                id = label.id,
                name = label.name,
                disambiguation = label.disambiguation,
                type = label.type,
                labelCode = label.labelCode,
                catalogNumber = labelInfo.catalogNumber
            )
        }
    }
}
