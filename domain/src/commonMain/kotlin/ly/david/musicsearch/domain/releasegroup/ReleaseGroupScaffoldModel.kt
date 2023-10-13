package ly.david.musicsearch.domain.releasegroup

import ly.david.musicsearch.data.core.releasegroup.ReleaseGroup
import ly.david.musicsearch.data.core.releasegroup.ReleaseGroupForDetails
import ly.david.musicsearch.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.domain.artist.toArtistCreditUiModel
import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import ly.david.musicsearch.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Artist_credit_name
import lydavidmusicsearchdatadatabase.Relation

data class ReleaseGroupScaffoldModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String = "",
    override val disambiguation: String = "",
    override val primaryType: String? = null,
    override val secondaryTypes: List<String>? = null,
    val imageUrl: String? = null,
    val artistCredits: List<ArtistCreditUiModel> = listOf(),
    val urls: List<RelationListItemModel> = listOf(),
) : ReleaseGroup

internal fun ReleaseGroupForDetails.toReleaseGroupScaffoldModel(
    artistCreditNames: List<Artist_credit_name>,
    urls: List<Relation>,
) = ReleaseGroupScaffoldModel(
    id = id,
    name = name,
    firstReleaseDate = firstReleaseDate,
    disambiguation = disambiguation,
    primaryType = primaryType,
    secondaryTypes = secondaryTypes,
    imageUrl = imageUrl,
    artistCredits = artistCreditNames.map { it.toArtistCreditUiModel() },
    urls = urls.map { it.toRelationListItemModel() },
)
