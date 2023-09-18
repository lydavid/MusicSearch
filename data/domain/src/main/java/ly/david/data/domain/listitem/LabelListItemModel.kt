package ly.david.data.domain.listitem

import ly.david.data.musicbrainz.LabelInfo
import ly.david.data.musicbrainz.LabelMusicBrainzModel
import ly.david.data.room.release.LabelWithCatalog
import lydavidmusicsearchdatadatabase.Label

data class LabelListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val labelCode: Int? = null,

    // TODO: don't need this in scaffold model
    val catalogNumber: String? = null,
) : ly.david.data.core.Label, ListItemModel()

internal fun LabelMusicBrainzModel.toLabelListItemModel() =
    LabelListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode
    )

fun Label.toLabelListItemModel() =
    LabelListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = label_code,
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
