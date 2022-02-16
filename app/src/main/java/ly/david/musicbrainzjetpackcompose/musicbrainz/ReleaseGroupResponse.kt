package ly.david.musicbrainzjetpackcompose.musicbrainz

import com.squareup.moshi.Json

//{
//    "releases": [
//    {
//        "status": "Official",
//        "packaging": null,
//        "country": "JP",
//        "quality": "normal",
//        "disambiguation": "初回限定盤",
//        "text-representation": {
//        "language": "jpn",
//        "script": "Jpan"
//    },
//        "packaging-id": null,
//        "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
//        "title": "欠けた心象、世のよすが",
//        "release-events": [
//        {
//            "date": "2021-09-08",
//            "area": {
//            "disambiguation": "",
//            "id": "2db42837-c832-3c27-b4a3-08198f75693c",
//            "sort-name": "Japan",
//            "type": null,
//            "type-id": null,
//            "iso-3166-1-codes": [
//            "JP"
//            ],
//            "name": "Japan"
//        }
//        }
//        ],
//        "id": "f171e0ae-bea8-41e6-bb41-4c7af7977f50",
//        "media": [
//        {
//            "title": "",
//            "track-count": 8,
//            "position": 1,
//            "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
//            "format": "CD"
//        }
//        ],
//        "date": "2021-09-08",
//        "barcode": "4988002911981"
//    },
//    {
//        "disambiguation": "",
//        "text-representation": {
//        "script": "Jpan",
//        "language": "jpn"
//    },
//        "packaging-id": null,
//        "status": "Official",
//        "packaging": null,
//        "country": "JP",
//        "quality": "normal",
//        "id": "165f6643-2edb-4795-9abe-26bd0533e59d",
//        "date": "2021-09-08",
//        "media": [
//        {
//            "title": "",
//            "format": "CD",
//            "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
//            "position": 1,
//            "track-count": 8
//        }
//        ],
//        "barcode": "4988002911998",
//        "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
//        "title": "欠けた心象、世のよすが",
//        "release-events": [
//        {
//            "date": "2021-09-08",
//            "area": {
//            "iso-3166-1-codes": [
//            "JP"
//            ],
//            "name": "Japan",
//            "type-id": null,
//            "type": null,
//            "disambiguation": "",
//            "id": "2db42837-c832-3c27-b4a3-08198f75693c",
//            "sort-name": "Japan"
//        }
//        }
//        ]
//    },
//    {
//        "text-representation": {
//        "script": "Jpan",
//        "language": "jpn"
//    },
//        "packaging-id": null,
//        "disambiguation": "アニメイト Ver.",
//        "quality": "normal",
//        "country": "JP",
//        "packaging": null,
//        "status": "Official",
//        "barcode": "4988002911998",
//        "media": [
//        {
//            "format": "CD",
//            "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
//            "position": 1,
//            "track-count": 8,
//            "title": ""
//        },
//        {
//            "position": 2,
//            "track-count": 3,
//            "format": "CD",
//            "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
//            "title": "アニメイト Bonus CD"
//        }
//        ],
//        "date": "2021-09-08",
//        "id": "f81cbdf9-4390-4738-b6b2-124f5bceafe3",
//        "title": "欠けた心象、世のよすが",
//        "release-events": [
//        {
//            "area": {
//            "type-id": null,
//            "iso-3166-1-codes": [
//            "JP"
//            ],
//            "name": "Japan",
//            "disambiguation": "",
//            "sort-name": "Japan",
//            "id": "2db42837-c832-3c27-b4a3-08198f75693c",
//            "type": null
//        },
//            "date": "2021-09-08"
//        }
//        ],
//        "status-id": "4e304316-386d-3409-af2e-78857eec5cfe"
//    },
//    {
//        "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
//        "release-events": [
//        {
//            "area": {
//            "iso-3166-1-codes": [
//            "XW"
//            ],
//            "name": "[Worldwide]",
//            "type-id": null,
//            "type": null,
//            "disambiguation": "",
//            "sort-name": "[Worldwide]",
//            "id": "525d4e18-3d00-31b9-a58b-a146a916de8f"
//        },
//            "date": "2021-09-08"
//        }
//        ],
//        "title": "欠けた心象、世のよすが",
//        "id": "a3e0f12c-331a-4082-a244-baed958e78b8",
//        "media": [
//        {
//            "title": "",
//            "format": "Digital Media",
//            "format-id": "907a28d9-b3b2-3ef6-89a8-7b18d91d4794",
//            "position": 1,
//            "track-count": 8
//        }
//        ],
//        "date": "2021-09-08",
//        "barcode": "4988002911998",
//        "status": "Official",
//        "packaging": "None",
//        "country": "XW",
//        "quality": "normal",
//        "disambiguation": "",
//        "packaging-id": "119eba76-b343-3e02-a292-f0f00644bb9b",
//        "text-representation": {
//        "language": "jpn",
//        "script": "Jpan"
//    }
//    },
//    {
//        "id": "d7c52617-8976-484d-af73-dc76116ca131",
//        "disambiguation": "",
//        "media": [
//        {
//            "position": 1,
//            "track-count": 8,
//            "format": "Digital Media",
//            "format-id": "907a28d9-b3b2-3ef6-89a8-7b18d91d4794",
//            "title": ""
//        }
//        ],
//        "barcode": null,
//        "text-representation": {
//        "script": null,
//        "language": null
//    },
//        "packaging-id": null,
//        "status-id": "41121bb9-3413-3818-8a9a-9742318349aa",
//        "status": "Pseudo-Release",
//        "packaging": null,
//        "title": "Crescent",
//        "quality": "normal"
//    }
//    ],
//    "id": "81d75493-78b6-4a37-b5ae-2a3918ee3756",
//    "secondary-types": [],
//    "disambiguation": "",
//    "primary-type-id": "6d0c5bf6-7a33-3420-a519-44fc63eedebf",
//    "first-release-date": "2021-09-08",
//    "primary-type": "EP",
//    "title": "欠けた心象、世のよすが",
//    "artist-credit": [
//    {
//        "artist": {
//        "type": "Group",
//        "disambiguation": "",
//        "id": "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
//        "sort-name": "Tsukuyomi",
//        "name": "月詠み",
//        "type-id": "e431f5f6-b5d2-343d-8b36-72607fffb74b"
//    },
//        "joinphrase": "",
//        "name": "月詠み"
//    }
//    ],
//    "secondary-type-ids": []
//}

// TODO: this is actually just ReleaseGroup but with more fields
data class ReleaseGroupResponse( // TODO: rename without browse, generalize for lookup/browse. lookup gives same info as browse but more
//    @Json(name = "release-count") val releaseCount: Int,
//    @Json(name = "release-offset") val releaseOffset: Int,

    // lookup inc=releases
    @Json(name = "releases") val releases: List<Release>? = null,

    // lookup inc=artists
    @Json(name = "artist-credit") val artistCredit: ArtistCredit? = null
)

data class ArtistCredit(
    @Json(name = "artist") val artist: Artist, // maybe too much data?
    @Json(name = "joinphrase") val joinPhrase: String,
    @Json(name = "name") val name: String,
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
    @Json(name = "release-events") val releaseEvents: List<ReleaseEvent>? = null,

    // lookup inc=media
    @Json(name = "media") val media: List<Media>? = null
)
//"media": [
//{
//    "format": "CD",
//    "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
//    "position": 1,
//    "track-count": 8,
//    "title": ""
//},
//{
//    "position": 2,
//    "track-count": 3,
//    "format": "CD",
//    "format-id": "9712d52a-4509-3d4b-a1a2-67c88c643e31",
//    "title": "アニメイト Bonus CD"
//}
//],

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

data class Media(
    @Json(name = "title") val title: String? = null,
    @Json(name = "track-count") val trackCount: Int,
    @Json(name = "position") val position: Int,
    @Json(name = "format") val format: String? = null,
    @Json(name = "format-id") val formatId: String? = null
)
