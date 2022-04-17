package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Recording
import ly.david.mbjc.data.network.RecordingMusicBrainzModel
import ly.david.mbjc.data.persistence.RecordingRoomModel

internal data class RecordingUiModel(
    override val id: String,
    override val name: String,
    override val date: String?,
    override val disambiguation: String,
    override val length: Int?,
    override val video: Boolean
) : UiModel(), Recording

internal fun RecordingMusicBrainzModel.toRecordingUiModel() =
    RecordingRoomModel(
        id = id,
        name = name,
        date = date,
        disambiguation = disambiguation,
        length = length,
        video = video
    )
