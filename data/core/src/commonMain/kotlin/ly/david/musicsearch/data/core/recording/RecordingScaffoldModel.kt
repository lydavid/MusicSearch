package ly.david.musicsearch.data.core.recording

import ly.david.musicsearch.data.core.Recording
import ly.david.musicsearch.data.core.artist.ArtistCreditUiModel
import ly.david.musicsearch.data.core.listitem.RelationListItemModel

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
