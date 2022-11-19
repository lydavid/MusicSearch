package ly.david.data.domain

import ly.david.data.Recording
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.getReleaseArtistCreditRoomModels
import ly.david.data.persistence.recording.RecordingArtistCreditRoomModel
import ly.david.data.persistence.recording.RecordingRoomModel

data class RecordingUiModel(
    override val id: String,
    override val name: String,
    override val date: String?,
    override val disambiguation: String,
    override val length: Int?,
    override val video: Boolean,
    val artistCredits: List<RecordingArtistCreditRoomModel> = listOf(),
) : UiModel(), Recording

internal fun RecordingMusicBrainzModel.toRecordingUiModel() =
    RecordingUiModel(
        id = id,
        name = name,
        date = date,
        disambiguation = disambiguation,
        length = length,
        video = video ?: false,
        artistCredits = getReleaseArtistCreditRoomModels()
    )

fun RecordingRoomModel.toRecordingUiModel(
    artistCredits: List<RecordingArtistCreditRoomModel>
) = RecordingUiModel(
    id = id,
    name = name,
    date = date,
    disambiguation = disambiguation,
    length = length,
    video = video,
    artistCredits = artistCredits
)
