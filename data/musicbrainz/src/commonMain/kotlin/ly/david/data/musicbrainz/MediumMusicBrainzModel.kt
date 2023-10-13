package ly.david.data.musicbrainz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.core.Medium

/**
 * A [Medium] in [ReleaseMusicBrainzModel].
 */
@Serializable
data class MediumMusicBrainzModel(
    @SerialName("position") override val position: Int? = null,
    @SerialName("title") override val title: String? = null,
    @SerialName("track-count") override val trackCount: Int,
    @SerialName("format") override val format: String? = null,
    @SerialName("format-id") val formatId: String? = null,
//    @SerialName("track-offset") val trackOffset: Int = 0, // currently doesn't seem like we need to use

    @SerialName("tracks") val tracks: List<TrackMusicBrainzModel>? = null,
) : Medium
