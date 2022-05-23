package ly.david.mbjc.data

import com.squareup.moshi.Json

//"area": {
//    "type": null,
//    "name": "Japan",
//    "sort-name": "Japan",
//    "disambiguation": "",
//    "id": "2db42837-c832-3c27-b4a3-08198f75693c",
//    "type-id": null,
//    "iso-3166-1-codes": [
//    "JP"
//    ]
//},
internal data class Area(
    @Json(name = "id") val id: String,
    @Json(name = "iso-3166-1-codes") val isoCodes: List<String>? = null,
)
