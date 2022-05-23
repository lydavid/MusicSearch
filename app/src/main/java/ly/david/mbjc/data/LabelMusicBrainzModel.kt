package ly.david.mbjc.data

import com.squareup.moshi.Json

internal data class LabelInfo(
    @Json(name = "catalog-number") val catalogNumber: String? = null,
    @Json(name = "label") val label: LabelMusicBrainzModel? = null,
)

internal data class LabelMusicBrainzModel(
    @Json(name = "label-code") val labelCode: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "id") val id: String,
    @Json(name = "disambiguation") override val disambiguation: String? = null,
    @Json(name = "name") override val name: String? = null,
    @Json(name = "sort-name") val sortName: String? = null,
    @Json(name = "type") val type: String? = null,
): NameWithDisambiguation
