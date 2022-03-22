package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.Recording
import ly.david.mbjc.data.Track

/**
 * A [Track] in [MusicBrainzMedium].
 */
data class MusicBrainzTrack(
    @Json(name = "id") override val id: String,
    @Json(name = "position") override val position: Int,
    @Json(name = "number") override val number: String, // Usually a string of `position`, but could be things like `A1`
    @Json(name = "title") override val title: String,
    @Json(name = "length") override val length: Int? = null,

    @Json(name = "artist-credit") val artistCredits: List<MusicBrainzArtistCredit>? = null,
    @Json(name = "recording") val recording: Recording,
//    @Json(name = "relations") val relations: List<Relation>? = null,
) : Track
