package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Medium
import ly.david.mbjc.data.Track
import ly.david.mbjc.data.persistence.release.MediumRoomModel
import ly.david.mbjc.data.persistence.release.TrackRoomModel

/**
 * [Medium] to display in UI, along with its [Track]s.
 */
internal data class MediumUiModel(
    override val position: Int,
    override val title: String,
    override val trackCount: Int,
    override val format: String?,
    val tracks: List<TrackUiModel>
) : Medium

internal fun MediumRoomModel.toMediumUiModel(tracks: List<TrackUiModel>) =
    MediumUiModel(
        position = position,
        title = title,
        trackCount = trackCount,
        format = format,
        tracks = tracks
    )

internal fun TrackRoomModel.toTrackUiModel() =
    TrackUiModel(
        id = id,
        position = position,
        number = number,
        title = title,
        length = length
    )
