package ly.david.musicbrainzjetpackcompose.musicbrainz

import com.squareup.moshi.Json

//{
//    "release-group-count": 3,
//    "release-groups": [
//    {
//        "first-release-date": "2022-01-26",
//        "title": "生きるよすが",
//        "secondary-type-ids": [],
//        "secondary-types": [],
//        "id": "115f3d4f-7b9e-49e9-b13f-d1960415fd33",
//        "disambiguation": "",
//        "primary-type": "Single",
//        "primary-type-id": "d6038452-8ee0-3f68-affc-2de9a1ede0b9"
//    },
//    {
//        "secondary-types": [],
//        "primary-type-id": "d6038452-8ee0-3f68-affc-2de9a1ede0b9",
//        "id": "7020b770-eb2c-44c3-8968-5fecee4b54e9",
//        "disambiguation": "",
//        "primary-type": "Single",
//        "first-release-date": "2020-10-11",
//        "title": "こんな命がなければ",
//        "secondary-type-ids": []
//    },
//    {
//        "primary-type-id": "6d0c5bf6-7a33-3420-a519-44fc63eedebf",
//        "primary-type": "EP",
//        "disambiguation": "",
//        "id": "81d75493-78b6-4a37-b5ae-2a3918ee3756",
//        "secondary-types": [],
//        "secondary-type-ids": [],
//        "title": "欠けた心象、世のよすが",
//        "first-release-date": "2021-09-08"
//    }
//    ],
//    "release-group-offset": 0
//}
data class ReleaseGroups(
    @Json(name = "release-group-count") val releaseGroupCount: Int,
    @Json(name = "release-groups") val releaseGroups: List<ReleaseGroup>
)

data class ReleaseGroup(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "first-release-date") val firstReleaseDate: String,
    @Json(name = "disambiguation") val disambiguation: String = "",
    @Json(name = "primary-type") val primaryType: String? = null,
    @Json(name = "primary-type-id") val primaryTypeId: String? = null,
)

private const val YEAR_FIRST_INDEX = 0
private const val YEAR_LAST_INDEX = 4

fun ReleaseGroup.getYear(): String =
    if (firstReleaseDate.length < YEAR_LAST_INDEX) {
        ""
    } else {
        firstReleaseDate.substring(YEAR_FIRST_INDEX, YEAR_LAST_INDEX)
    }
