package ly.david.mbjc.data

import com.squareup.moshi.Json
import ly.david.mbjc.data.network.MusicBrainzArtist

data class Relation(
    @Json(name = "type") val type: String? = null,
    @Json(name = "target-type") val targetType: String, // artist, place, work, label
    @Json(name = "target-credit") val targetCredit: String? = null, // prefer this credit over object's name
    @Json(name = "artist") val artist: MusicBrainzArtist? = null, // could be composer, arranger, etc
    @Json(name = "label") val label: Label? = null,
    @Json(name = "work") val work: Work? = null,
    // Place
    @Json(name = "attributes") val attributes: List<String>? = null, // strings
)
