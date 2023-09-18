package ly.david.data.domain.recordng

import ly.david.data.domain.artist.ArtistCreditUiModel
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Artist_credit_name
import lydavidmusicsearchdatadatabase.Mb_relation
import lydavidmusicsearchdatadatabase.Recording

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
    urls: List<Mb_relation>,
) = RecordingScaffoldModel(
    id = id,
    name = name,
    firstReleaseDate = first_release_date,
    disambiguation = disambiguation,
    length = length,
    video = video,
    isrcs = isrcs,
    artistCredits = artistCreditNames.map { artistCreditName ->
        ArtistCreditUiModel(
            artistId = artistCreditName.artist_id,
            name = artistCreditName.name,
            joinPhrase = artistCreditName.join_phrase,
        )
    },
    urls = urls.map { it.toRelationListItemModel() },
)
