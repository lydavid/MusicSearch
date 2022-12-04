package ly.david.data.domain

import ly.david.data.Recording
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.persistence.artist.credit.toUiModels
import ly.david.data.persistence.recording.RecordingForScaffold

data class RecordingScaffoldModel(
    override val id: String,
    override val name: String,
    override val date: String? = null,
    override val disambiguation: String = "",
    override val length: Int? = null,
    override val video: Boolean = false,
    val artistCredits: List<ArtistCreditUiModel> = listOf(),
) : Recording

fun RecordingForScaffold.toScaffoldModel() = RecordingScaffoldModel(
    id = recording.id,
    name = recording.name,
    date = recording.date,
    disambiguation = recording.disambiguation,
    length = recording.length,
    video = recording.video,
    artistCredits = artistCreditNamesWithResources.toUiModels()
)

fun RecordingMusicBrainzModel.toScaffoldModel() = RecordingScaffoldModel(
    id = id,
    name = name,
    date = date,
    disambiguation = disambiguation,
    length = length,
    video = video ?: false,
    artistCredits = artistCredits.toUiModels()
)
