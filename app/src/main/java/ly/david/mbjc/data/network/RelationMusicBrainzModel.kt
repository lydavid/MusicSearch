package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.Label
import ly.david.mbjc.data.Work

internal data class RelationMusicBrainzModel(
    @Json(name = "type")
    val type: String? = null,
    @Json(name = "target-type")
    val targetType: MusicBrainzResource, // artist, place, work, label
    @Json(name = "target-credit")
    val targetCredit: String? = null, // prefer this credit over object's name if it exists
    @Json(name = "attributes")
    val attributes: List<String>? = null, // strings, lead vocals

    @Json(name = "artist")
    val artist: ArtistMusicBrainzModel? = null, // could be composer, arranger, etc
    @Json(name = "label")
    val label: Label? = null,
    @Json(name = "work")
    val work: Work? = null,

    // Place

)
