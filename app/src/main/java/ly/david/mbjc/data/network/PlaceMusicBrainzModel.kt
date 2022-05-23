package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.Area
import ly.david.mbjc.data.NameWithDisambiguation

internal data class PlaceMusicBrainzModel(
    @Json(name = "id")
    val id: String,

    @Json(name = "name")
    override val name: String? = null,

    @Json(name = "disambiguation")
    override val disambiguation: String = "",

    @Json(name = "address")
    val address: String? = null,

    /**
     * ex. "Studio"
     */
    @Json(name = "type")
    val type: String? = null,

    @Json(name = "type_id")
    val typeId: String? = null,

    @Json(name = "coordinates")
    val coordinates: Coordinates? = null,

    @Json(name = "area")
    val area: Area? = null,
): NameWithDisambiguation

internal data class Coordinates(
    @Json(name = "longitude")
    val longitude: Float,

    @Json(name = "latitude")
    val latitude: Float,
)
