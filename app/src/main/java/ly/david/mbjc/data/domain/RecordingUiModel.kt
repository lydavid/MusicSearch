package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Recording
import ly.david.mbjc.data.network.RecordingMusicBrainzModel

internal data class RecordingUiModel(
    override val id: String,
    override val name: String,
    override val date: String?,
    override val disambiguation: String,
    override val length: Int?,
    override val video: Boolean
) : UiModel(), Recording

internal fun RecordingMusicBrainzModel.toRecordingUiModel() =
    RecordingUiModel(
        id = id,
        name = name,
        date = date,
        disambiguation = disambiguation,
        length = length,
        video = video ?: false
    )
