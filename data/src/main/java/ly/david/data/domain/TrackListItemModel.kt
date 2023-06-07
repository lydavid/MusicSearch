package ly.david.data.domain

import ly.david.data.Track
import ly.david.data.room.release.tracks.TrackForListItem

data class TrackListItemModel(
    override val id: String,
    override val position: Int,
    override val number: String,
    override val title: String,
    override val length: Int? = null,
    val mediumId: Long,
    val recordingId: String,

    val formattedArtistCredits: String? = null,
) : ListItemModel(), Track

fun TrackForListItem.toTrackListItemModel() =
    TrackListItemModel(
        id = track.id,
        position = track.position,
        number = track.number,
        title = track.title,
        length = track.length,
        mediumId = track.mediumId,
        recordingId = track.recordingId,
        formattedArtistCredits = artistCreditNames
    )
