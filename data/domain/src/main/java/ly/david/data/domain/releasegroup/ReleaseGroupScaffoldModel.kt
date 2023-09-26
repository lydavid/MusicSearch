package ly.david.data.domain.releasegroup

import ly.david.data.core.releasegroup.ReleaseGroup
import ly.david.data.domain.artist.ArtistCreditUiModel
import ly.david.data.domain.artist.toArtistCreditUiModel
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Artist_credit_name
import lydavidmusicsearchdatadatabase.Relation
import lydavidmusicsearchdatadatabase.Release_group

data class ReleaseGroupScaffoldModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String = "",
    override val disambiguation: String = "",
    override val primaryType: String? = null,
    override val secondaryTypes: List<String>? = null,
    val artistCredits: List<ArtistCreditUiModel> = listOf(),
    val imageUrl: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ReleaseGroup

internal fun Release_group.toReleaseGroupScaffoldModel(
    artistCreditNames: List<Artist_credit_name>,
    imageUrl: String?,
    urls: List<Relation>,
) = ReleaseGroupScaffoldModel(
    id = id,
    name = name,
    firstReleaseDate = first_release_date,
    disambiguation = disambiguation,
    primaryType = primary_type,
    secondaryTypes = secondary_types,
    artistCredits = artistCreditNames.map { it.toArtistCreditUiModel() },
    imageUrl = imageUrl,
    urls = urls.map { it.toRelationListItemModel() },
)
