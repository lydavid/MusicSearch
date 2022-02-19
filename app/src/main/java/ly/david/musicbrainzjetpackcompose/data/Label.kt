package ly.david.musicbrainzjetpackcompose.data

import com.squareup.moshi.Json

data class LabelInfo(
    @Json(name = "catalog-number") val catalogNumber: String? = null,
    @Json(name = "label") val label: Label? = null,
)

data class Label(
    @Json(name = "label-code") val labelCode: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "id") val id: String? = null,
    @Json(name = "disambiguation") val disambiguation: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "sort-name") val sortName: String? = null,
    @Json(name = "type") val type: String? = null,
)
