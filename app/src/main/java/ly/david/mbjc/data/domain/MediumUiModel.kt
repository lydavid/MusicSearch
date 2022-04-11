package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Medium
import ly.david.mbjc.data.Track
import ly.david.mbjc.data.persistence.release.RoomMedium
import ly.david.mbjc.data.persistence.release.RoomTrack

/**
 * [Medium] to display in UI, along with its [Track]s.
 */
data class MediumUiModel(
    override val position: Int,
    override val title: String,
    override val trackCount: Int,
    override val format: String?,
    val tracks: List<TrackUiModel>
) : Medium

fun RoomMedium.toMediumUiModel(tracks: List<TrackUiModel>) =
    MediumUiModel(
        position = position,
        title = title,
        trackCount = trackCount,
        format = format,
        tracks = tracks
    )

fun RoomTrack.toTrackUiModel() =
    TrackUiModel(
        id = id,
        position = position,
        number = number,
        title = title,
        length = length
    )
