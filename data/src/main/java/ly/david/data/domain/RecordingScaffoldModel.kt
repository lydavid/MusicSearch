package ly.david.data.domain

import ly.david.data.Recording
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.persistence.artist.credit.toArtistCreditUiModels
import ly.david.data.persistence.recording.RecordingForScaffold

data class RecordingScaffoldModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String? = null,
    override val disambiguation: String = "",
    override val length: Int? = null,
    override val video: Boolean = false,
    val isrcs: List<String>? = null,
    val artistCredits: List<ArtistCreditUiModel> = listOf(),
) : Recording

fun RecordingForScaffold.toRecordingScaffoldModel() = RecordingScaffoldModel(
    id = recording.id,
    name = recording.name,
    firstReleaseDate = recording.firstReleaseDate,
    disambiguation = recording.disambiguation,
    length = recording.length,
    video = recording.video,
    isrcs = recording.isrcs,
    artistCredits = artistCreditNamesWithResources.toArtistCreditUiModels()
)

fun RecordingMusicBrainzModel.toRecordingScaffoldModel() = RecordingScaffoldModel(
    id = id,
    name = name,
    firstReleaseDate = firstReleaseDate,
    disambiguation = disambiguation,
    length = length,
    video = video,
    isrcs = isrcs,
    artistCredits = artistCredits.toArtistCreditUiModels()
)
