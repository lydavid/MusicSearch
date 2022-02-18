package ly.david.musicbrainzjetpackcompose.data

import com.squareup.moshi.Json

data class ReleaseGroup(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "first-release-date") val firstReleaseDate: String,
    @Json(name = "disambiguation") val disambiguation: String = "",
    @Json(name = "primary-type") val primaryType: String? = null,
    @Json(name = "primary-type-id") val primaryTypeId: String? = null,
    @Json(name = "secondary-types") val secondaryTypes: List<String>? = null,
    @Json(name = "secondary-type-ids") val secondaryTypeIds: List<String>? = null,

    // lookup inc=releases
    @Json(name = "releases") val releases: List<Release>? = null,

    // lookup inc=artists
    @Json(name = "artist-credit") val artistCredit: List<ArtistCredit>? = null
)

data class ArtistCredit(
    @Json(name = "artist") val artist: Artist,
    @Json(name = "joinphrase") val joinPhrase: String,
    @Json(name = "name") val name: String,
)
