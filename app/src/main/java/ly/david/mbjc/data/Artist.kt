package ly.david.mbjc.data

import com.squareup.moshi.Json

data class Artist(
    @Json(name = "id") val id: String,

    @Json(name = "name") val name: String,
    @Json(name = "sort-name") val sortName: String,
    @Json(name = "disambiguation") val disambiguation: String? = null,

    @Json(name = "type") val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,

    @Json(name = "gender") val gender: String? = null,
    @Json(name = "gender-id") val genderId: String? = null,

    @Json(name = "country") val country: String? = null,

    @Json(name = "life-span") val lifeSpan: LifeSpan? = null,

    // for search responses only
    @Json(name = "score") val score: Int? = null,
)

data class LifeSpan(
    @Json(name = "begin") val begin: String? = null,
    @Json(name = "ended") val ended: Boolean? = null
)
