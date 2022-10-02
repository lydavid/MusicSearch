package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Track
import ly.david.mbjc.data.persistence.release.TrackRoomModel

internal data class TrackUiModel(
    override val id: String,
    override val position: Int,
    override val number: String,
    override val title: String,
    override val length: Int?,
    val mediumId: Long,
    val recordingId: String
) : UiModel(), Track

internal fun TrackRoomModel.toTrackUiModel() =
    TrackUiModel(
        id = id,
        position = position,
        number = number,
        title = title,
        length = length,
        mediumId = mediumId,
        recordingId = recordingId
    )
