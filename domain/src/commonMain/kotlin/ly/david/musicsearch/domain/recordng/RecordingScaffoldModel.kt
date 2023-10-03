package ly.david.musicsearch.domain.recordng

import ly.david.musicsearch.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.domain.artist.toArtistCreditUiModel
import ly.david.musicsearch.domain.listitem.RelationListItemModel
import ly.david.musicsearch.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Artist_credit_name
import lydavidmusicsearchdatadatabase.Recording
import lydavidmusicsearchdatadatabase.Relation

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
) : ly.david.data.core.Recording

internal fun Recording.toRecordingScaffoldModel(
    artistCreditNames: List<Artist_credit_name>,
    urls: List<Relation>,
) = RecordingScaffoldModel(
    id = id,
    name = name,
    firstReleaseDate = first_release_date,
    disambiguation = disambiguation,
    length = length,
    video = video,
    isrcs = isrcs,
    artistCredits = artistCreditNames.map { it.toArtistCreditUiModel() },
    urls = urls.map { it.toRelationListItemModel() },
)
