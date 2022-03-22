package ly.david.mbjc.data.domain.sub

import ly.david.mbjc.data.Medium
import ly.david.mbjc.data.Track
import ly.david.mbjc.data.persistence.release.RoomMedium
import ly.david.mbjc.data.persistence.release.RoomTrack

/**
 * [Medium] to display in UI, along with its [Track]s.
 */
data class UiMedium(
    override val position: Int,
    override val title: String,
    override val trackCount: Int,
    override val format: String?,
    val tracks: List<UiTrack>
) : Medium

fun RoomMedium.toUiMedium(tracks: List<UiTrack>) =
    UiMedium(
        position = position,
        title = title,
        trackCount = trackCount,
        format = format,
        tracks = tracks
    )

data class UiTrack(
    override val id: String,
    override val position: Int,
    override val number: String,
    override val title: String,
    override val length: Int?
) : Track

fun RoomTrack.toUiTrack() =
    UiTrack(
        id = id,
        position = position,
        number = number,
        title = title,
        length = length
    )
