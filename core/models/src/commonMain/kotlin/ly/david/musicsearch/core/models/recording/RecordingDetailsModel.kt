package ly.david.musicsearch.core.models.recording

import ly.david.musicsearch.core.models.artist.ArtistCreditUiModel
import ly.david.musicsearch.core.models.listitem.RelationListItemModel

data class RecordingDetailsModel(
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
