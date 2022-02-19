package ly.david.musicbrainzjetpackcompose.data

import com.squareup.moshi.Json

data class Track(
    @Json(name = "id") val id: String,
    @Json(name = "position") val position: Int,
    @Json(name = "number") val number: String? = null, // Seems to be a string of position
    @Json(name = "length") val length: Int,
    @Json(name = "title") val title: String? = null,
    @Json(name = "artist-credit") val artistCredit: List<ArtistCredit>? = null,
    @Json(name = "recording") val recording: Recording,
    @Json(name = "relations") val relations: List<Relation>? = null,
)

data class Relation(
    @Json(name = "type") val type: String? = null,
    @Json(name = "target-type") val targetType: String, // artist, place, work, label
    @Json(name = "target-credit") val targetCredit: String? = null, // prefer this credit over object's name
    @Json(name = "artist") val artist: Artist? = null, // could be composer, arranger, etc
    @Json(name = "label") val label: Label? = null,
    // Place
    // Work
    @Json(name = "attributes") val attributes: List<String>? = null, // strings
)
