package ly.david.musicsearch.data.core.releasegroup

import ly.david.musicsearch.data.core.artist.ArtistCreditUiModel
import ly.david.musicsearch.data.core.listitem.RelationListItemModel

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
