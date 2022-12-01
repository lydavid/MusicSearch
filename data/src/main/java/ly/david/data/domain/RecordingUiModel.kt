package ly.david.data.domain

import ly.david.data.Recording
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.persistence.artist.credit.toUiModels
import ly.david.data.persistence.recording.RecordingRoomModel
import ly.david.data.persistence.recording.RecordingWithArtistCredits

data class RecordingUiModel(
    override val id: String,
    override val name: String,
    override val date: String?,
    override val disambiguation: String,
    override val length: Int?,
    override val video: Boolean,
    val artistCredits: List<ArtistCreditUiModel> = listOf(),
) : UiModel(), Recording

fun RecordingMusicBrainzModel.toRecordingUiModel() =
    RecordingUiModel(
        id = id,
        name = name,
        date = date,
        disambiguation = disambiguation,
        length = length,
        video = video ?: false,
        artistCredits = artistCredits.toUiModels()
    )

fun RecordingRoomModel.toRecordingUiModel() = RecordingUiModel(
    id = id,
    name = name,
    date = date,
    disambiguation = disambiguation,
    length = length,
    video = video,
)

fun RecordingWithArtistCredits.toRecordingUiModel() = RecordingUiModel(
    id = recording.id,
    name = recording.name,
    date = recording.date,
    disambiguation = recording.disambiguation,
    length = recording.length,
    video = recording.video,
    artistCredits = artistCreditNamesWithResources.toUiModels()
)
