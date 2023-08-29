package ly.david.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.data.Medium

/**
 * A [Medium] in [ReleaseMusicBrainzModel].
 */
@Serializable
data class MediumMusicBrainzModel(
    @SerialName("position") override val position: Int?,
    @SerialName("title") override val title: String?,
    @SerialName("track-count") override val trackCount: Int,
    @SerialName("format") override val format: String? = null,
    @SerialName("format-id") val formatId: String? = null,
//    @SerialName("track-offset") val trackOffset: Int = 0, // currently doesn't seem like we need to use

    @SerialName("tracks") val tracks: List<TrackMusicBrainzModel>? = null,
) : Medium
