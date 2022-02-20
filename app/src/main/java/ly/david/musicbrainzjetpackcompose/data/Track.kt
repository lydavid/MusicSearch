package ly.david.musicbrainzjetpackcompose.data

import com.squareup.moshi.Json

data class Track(
    @Json(name = "id") val id: String,
    @Json(name = "recording") val recording: Recording,
    @Json(name = "position") val position: Int,
    @Json(name = "number") val number: String, // Usually a string of `position`, but could be things like `A1`
    @Json(name = "title") val title: String,
    @Json(name = "artist-credit") val artistCredit: List<ArtistCredit>? = null,
    @Json(name = "length") val length: Int? = null,

    @Json(name = "relations") val relations: List<Relation>? = null,
)

data class Relation(
    @Json(name = "type") val type: String? = null,
    @Json(name = "target-type") val targetType: String, // artist, place, work, label
    @Json(name = "target-credit") val targetCredit: String? = null, // prefer this credit over object's name
    @Json(name = "artist") val artist: Artist? = null, // could be composer, arranger, etc
    @Json(name = "label") val label: Label? = null,
    @Json(name = "work") val work: Work? = null,
    // Place
    @Json(name = "attributes") val attributes: List<String>? = null, // strings
)

data class Work(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,

    @Json(name = "type") val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,

    @Json(name = "disambiguation") val disambiguation: String? = null,

    @Json(name = "language") val language: String? = null,
    @Json(name = "languages") val languages: List<String>? = null,

    @Json(name = "relations") val relations: List<Relation>? = null,
)
