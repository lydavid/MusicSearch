package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Label
import ly.david.data.LifeSpan
import ly.david.data.persistence.label.LabelRoomModel
import ly.david.data.persistence.label.ReleaseLabel

data class LabelInfo(
    @Json(name = "catalog-number") val catalogNumber: String? = null,
    @Json(name = "label") val label: LabelMusicBrainzModel? = null,
)

data class LabelMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "disambiguation") override val disambiguation: String? = null,
    @Json(name = "name") override val name: String,
    @Json(name = "type") override val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "label-code") override val labelCode: Int? = null,
    @Json(name = "area") val area: AreaMusicBrainzModel? = null,
    @Json(name = "life-span") val lifeSpan: LifeSpan? = null,
    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null
) : MusicBrainzModel(), Label

fun List<LabelInfo>.toLabelRoomModels(): List<LabelRoomModel> {
    return this.mapNotNull { labelInfo ->
        val label = labelInfo.label
        if (label == null) {
            null
        } else {
            LabelRoomModel(
                id = label.id,
                name = label.name,
                disambiguation = label.disambiguation,
                type = label.type,
                labelCode = label.labelCode
            )
        }
    }
}

fun List<LabelInfo>.toReleaseLabels(releaseId: String, labelId: String? = null): List<ReleaseLabel> {
    return this
        .filter { it.label?.id == labelId || labelId == null }
        .mapNotNull { labelInfo ->
            val label = labelInfo.label
            if (label == null) {
                null
            } else {
                ReleaseLabel(releaseId, label.id, labelInfo.catalogNumber.orEmpty())
            }
        }
}
