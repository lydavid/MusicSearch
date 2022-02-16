package ly.david.musicbrainzjetpackcompose.musicbrainz

import com.squareup.moshi.Json

//{
//    "releases": [
//    {
//        "packaging-id": null,
//        "quality": "normal",
//        "text-representation": {
//        "script": "Jpan",
//        "language": "jpn"
//    },
//        "id": "165f6643-2edb-4795-9abe-26bd0533e59d",
//        "asin": "B098VQPDBN",
//        "packaging": null,
//        "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
//        "release-events": [
//        {
//            "area": {
//            "type-id": null,
//            "type": null,
//            "iso-3166-1-codes": [
//            "JP"
//            ],
//            "id": "2db42837-c832-3c27-b4a3-08198f75693c",
//            "disambiguation": "",
//            "name": "Japan",
//            "sort-name": "Japan"
//        },
//            "date": "2021-09-08"
//        }
//        ],
//        "country": "JP",
//        "title": "欠けた心象、世のよすが",
//        "disambiguation": "",
//        "date": "2021-09-08",
//        "status": "Official",
//        "cover-art-archive": {
//        "front": true,
//        "count": 1,
//        "darkened": false,
//        "artwork": true,
//        "back": false
//    },
//        "barcode": "4988002911998"
//    },
//    {
//        "release-events": [
//        {
//            "date": "2021-09-08",
//            "area": {
//            "name": "[Worldwide]",
//            "sort-name": "[Worldwide]",
//            "disambiguation": "",
//            "id": "525d4e18-3d00-31b9-a58b-a146a916de8f",
//            "type-id": null,
//            "type": null,
//            "iso-3166-1-codes": [
//            "XW"
//            ]
//        }
//        }
//        ],
//        "country": "XW",
//        "status": "Official",
//        "cover-art-archive": {
//        "count": 1,
//        "front": true,
//        "back": false,
//        "artwork": true,
//        "darkened": false
//    },
//        "barcode": "4988002911998",
//        "disambiguation": "",
//        "date": "2021-09-08",
//        "title": "欠けた心象、世のよすが",
//        "text-representation": {
//        "script": "Jpan",
//        "language": "jpn"
//    },
//        "id": "a3e0f12c-331a-4082-a244-baed958e78b8",
//        "quality": "normal",
//        "packaging-id": "119eba76-b343-3e02-a292-f0f00644bb9b",
//        "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
//        "packaging": "None",
//        "asin": null
//    },
//    {
//        "quality": "normal",
//        "id": "d7c52617-8976-484d-af73-dc76116ca131",
//        "text-representation": {
//        "language": null,
//        "script": null
//    },
//        "packaging-id": null,
//        "packaging": null,
//        "status-id": "41121bb9-3413-3818-8a9a-9742318349aa",
//        "asin": null,
//        "barcode": null,
//        "status": "Pseudo-Release",
//        "cover-art-archive": {
//        "back": false,
//        "darkened": false,
//        "artwork": false,
//        "count": 0,
//        "front": false
//    },
//        "title": "Crescent",
//        "disambiguation": ""
//    },
//    {
//        "barcode": "4988002911981",
//        "status": "Official",
//        "cover-art-archive": {
//        "darkened": false,
//        "artwork": false,
//        "back": false,
//        "front": false,
//        "count": 0
//    },
//        "date": "2021-09-08",
//        "disambiguation": "初回限定盤",
//        "title": "欠けた心象、世のよすが",
//        "country": "JP",
//        "release-events": [
//        {
//            "date": "2021-09-08",
//            "area": {
//            "id": "2db42837-c832-3c27-b4a3-08198f75693c",
//            "iso-3166-1-codes": [
//            "JP"
//            ],
//            "type": null,
//            "type-id": null,
//            "sort-name": "Japan",
//            "name": "Japan",
//            "disambiguation": ""
//        }
//        }
//        ],
//        "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
//        "packaging": null,
//        "asin": "B098VJNDLC",
//        "id": "f171e0ae-bea8-41e6-bb41-4c7af7977f50",
//        "text-representation": {
//        "script": "Jpan",
//        "language": "jpn"
//    },
//        "quality": "normal",
//        "packaging-id": null
//    },
//    {
//        "country": "JP",
//        "release-events": [
//        {
//            "date": "2021-09-08",
//            "area": {
//            "type-id": null,
//            "type": null,
//            "iso-3166-1-codes": [
//            "JP"
//            ],
//            "id": "2db42837-c832-3c27-b4a3-08198f75693c",
//            "disambiguation": "",
//            "name": "Japan",
//            "sort-name": "Japan"
//        }
//        }
//        ],
//        "disambiguation": "アニメイト Ver.",
//        "date": "2021-09-08",
//        "title": "欠けた心象、世のよすが",
//        "cover-art-archive": {
//        "front": true,
//        "count": 1,
//        "artwork": true,
//        "darkened": false,
//        "back": false
//    },
//        "status": "Official",
//        "barcode": "4988002911998",
//        "packaging-id": null,
//        "text-representation": {
//        "script": "Jpan",
//        "language": "jpn"
//    },
//        "id": "f81cbdf9-4390-4738-b6b2-124f5bceafe3",
//        "quality": "normal",
//        "asin": null,
//        "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
//        "packaging": null
//    }
//    ],
//    "release-count": 5,
//    "release-offset": 0
//}
data class BrowseReleasesResponse(
    @Json(name = "release-count") val releaseCount: Int,
    @Json(name = "release-offset") val releaseOffset: Int,
    @Json(name = "releases") val releases: List<Release>
)

class Release(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "disambiguation") val disambiguation: String = "",
    @Json(name = "date") val date: String? = null,
    @Json(name = "status") val status: String? = null,
    @Json(name = "barcode") val barcode: String? = null,
    @Json(name = "status-id") val statusId: String? = null,
    @Json(name = "country") val country: String? = null,
    @Json(name = "packaging") val packaging: String? = null,
    @Json(name = "packaging-id") val packagingId: String? = null,
    @Json(name = "asin") val asin: String? = null,
    @Json(name = "quality") val quality: String? = null,
    @Json(name = "cover-art-archive") val coverArtArchive: CoverArtArchive = CoverArtArchive(),
    @Json(name = "text-representation") val textRepresentation: TextRepresentation? = null,
    @Json(name = "release-events") val releaseEvents: List<ReleaseEvent>? = null
)

data class CoverArtArchive(
    @Json(name = "darkened") val darkened: Boolean = false,
    @Json(name = "artwork") val artwork: Boolean = false,
    @Json(name = "back") val back: Boolean = false,
    @Json(name = "front") val front: Boolean = false,
    @Json(name = "count") val count: Int = 0
)

data class TextRepresentation(
    @Json(name = "script") val script: String? = null,
    @Json(name = "language") val language: String? = null,
)

data class ReleaseEvent(
    @Json(name = "date") val date: String? = null,
    @Json(name = "area") val area: Area? = null,
    @Json(name = "type") val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "sort-name") val sortName: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "disambiguation") val disambiguation: String? = null,
)

data class Area(
    @Json(name = "id") val id: String,
    @Json(name = "iso-3166-1-codes") val isoCodes: List<String>,
)
