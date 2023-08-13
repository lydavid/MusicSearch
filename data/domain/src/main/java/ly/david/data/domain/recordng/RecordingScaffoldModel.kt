package ly.david.data.domain.recordng

import ly.david.data.Recording
import ly.david.data.domain.artist.ArtistCreditUiModel
import ly.david.data.domain.artist.toArtistCreditUiModel
import ly.david.data.domain.artist.toArtistCreditUiModels
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.room.recording.RecordingWithAllData

data class RecordingScaffoldModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String? = null,
    override val disambiguation: String = "",
    override val length: Int? = null,
    override val video: Boolean = false,
    val isrcs: List<String>? = null,
    val artistCredits: List<ArtistCreditUiModel> = listOf(),
    val urls: List<RelationListItemModel> = listOf(),
) : Recording

internal fun RecordingWithAllData.toRecordingScaffoldModel() = RecordingScaffoldModel(
    id = recording.id,
    name = recording.name,
    firstReleaseDate = recording.firstReleaseDate,
    disambiguation = recording.disambiguation,
    length = recording.length,
    video = recording.video,
    isrcs = recording.isrcs,
    artistCredits = artistCreditNamesWithEntities.map {
        it.artistCreditNameRoomModel.toArtistCreditUiModel()
    },
    urls = urls.map { it.relation.toRelationListItemModel() },
)

internal fun RecordingMusicBrainzModel.toRecordingScaffoldModel() = RecordingScaffoldModel(
    id = id,
    name = name,
    firstReleaseDate = firstReleaseDate,
    disambiguation = disambiguation,
    length = length,
    video = video ?: false,
    isrcs = isrcs,
    artistCredits = artistCredits.toArtistCreditUiModels()
)
