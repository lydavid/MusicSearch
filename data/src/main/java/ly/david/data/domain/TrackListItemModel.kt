package ly.david.data.domain

import ly.david.data.Track
import ly.david.data.persistence.release.TrackRoomModel

data class TrackListItemModel(
    override val id: String,
    override val position: Int,
    override val number: String,
    override val title: String,
    override val length: Int? = null,
    val mediumId: Long,
    val recordingId: String
) : ListItemModel(), Track

fun TrackRoomModel.toTrackListItemModel() =
    TrackListItemModel(
        id = id,
        position = position,
        number = number,
        title = title,
        length = length,
        mediumId = mediumId,
        recordingId = recordingId
    )
