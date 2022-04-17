package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.Medium

/**
 * A [Medium] in [ReleaseMusicBrainzModel].
 */
internal data class MediumMusicBrainzModel(
    @Json(name = "position") override val position: Int,
    @Json(name = "title") override val title: String,
    @Json(name = "track-count") override val trackCount: Int,
    @Json(name = "format") override val format: String? = null,
//    @Json(name = "format-id") val formatId: String? = null,
//    @Json(name = "track-offset") val trackOffset: Int = 0, // currently doesn't seem like we need to use

    @Json(name = "tracks") val tracks: List<TrackMusicBrainzModel>? = null,
) : Medium

/**
 * All formats in this list of media grouped together.
 *
 * Example returns:
 * * 170xCD
 * * 2×CD + Blu-ray
 */
internal fun List<MediumMusicBrainzModel>?.getFormatsForDisplay(): String? {

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
 * The number of tracks in each media in the list, in sequential order.
 *
 * Example returns:
 * * 23
 * * 15 + 8 + 24
 */
internal fun List<MediumMusicBrainzModel>?.getTracksForDisplay(): String? {
    val tracksForDisplay = this?.joinToString(" + ") {
        "${it.trackCount}"
    }
    return if (tracksForDisplay.isNullOrEmpty()) {
        null
    } else {
        tracksForDisplay
    }
}
