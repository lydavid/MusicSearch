package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Medium

/**
 * A [Medium] in [ReleaseMusicBrainzModel].
 */
data class MediumMusicBrainzModel(
    @Json(name = "position") override val position: Int?,
    @Json(name = "title") override val title: String?,
    @Json(name = "track-count") override val trackCount: Int,
    @Json(name = "format") override val format: String? = null,
//    @Json(name = "format-id") val formatId: String? = null,
//    @Json(name = "track-offset") val trackOffset: Int = 0, // currently doesn't seem like we need to use

    @Json(name = "tracks") val tracks: List<TrackMusicBrainzModel>? = null,
) : Medium
