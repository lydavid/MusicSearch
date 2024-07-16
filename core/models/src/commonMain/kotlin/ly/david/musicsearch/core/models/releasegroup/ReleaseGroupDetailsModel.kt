package ly.david.musicsearch.core.models.releasegroup

import ly.david.musicsearch.core.models.artist.ArtistCreditUiModel
import ly.david.musicsearch.core.models.listitem.RelationListItemModel

data class ReleaseGroupDetailsModel(
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
