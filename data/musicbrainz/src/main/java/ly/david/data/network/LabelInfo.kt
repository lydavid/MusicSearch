package ly.david.data.network

import com.squareup.moshi.Json

data class LabelInfo(
    @Json(name = "catalog-number") val catalogNumber: String? = null,
    @Json(name = "label") val label: LabelMusicBrainzModel? = null,
)
