package ly.david.musicbrainzjetpackcompose.data

import com.squareup.moshi.Json

data class Recording(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "first-release-date") val date: String? = null,
    @Json(name = "disambiguation") val disambiguation: String = "",
    @Json(name = "length") val length: Int? = null,
    @Json(name = "video") val video: Boolean = false,
    @Json(name = "artist-credit") val artistCredit: List<ArtistCredit>? = null,
)
