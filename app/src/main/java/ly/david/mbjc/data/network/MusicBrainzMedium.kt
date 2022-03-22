package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.Medium

/**
 * A [Medium] in [MusicBrainzRelease].
 */
data class MusicBrainzMedium(
    @Json(name = "position") override val position: Int,
    @Json(name = "title") override val title: String,
    @Json(name = "track-count") override val trackCount: Int,
    @Json(name = "format") override val format: String? = null,
//    @Json(name = "format-id") val formatId: String? = null,
//    @Json(name = "track-offset") val trackOffset: Int = 0, // currently doesn't seem like we need to use

    @Json(name = "tracks") val tracks: List<MusicBrainzTrack>? = null,
) : Medium

/**
 * @sample 170xCD
 * @sample 2×CD + Blu-ray
 */
fun List<MusicBrainzMedium>?.getFormatsForDisplay(): String? {

    val hashMap = hashMapOf<String, Int>()

    this?.forEach { medium ->
        medium.format?.let { format ->
            val currentCount = hashMap[format]
            if (currentCount == null) {
                hashMap[format] = 1
            } else {
                hashMap[format] = currentCount + 1
            }
        }
    }

    return if (hashMap.isEmpty()) {
        null
    } else {
        hashMap.map {
            val count = it.value
            if (count == 1) {
                it.key
            } else {
                "$count×${it.key}"
            }
        }.joinToString(" + ")
    }
}

/**
 * @sample 23
 * @sample 15 + 8 + 24
 */
fun List<MusicBrainzMedium>?.getTracksForDisplay(): String? {
    val tracksForDisplay = this?.joinToString(" + ") {
        "${it.trackCount}"
    }
    return if (tracksForDisplay.isNullOrEmpty()) {
        null
    } else {
        tracksForDisplay
    }
}
