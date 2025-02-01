package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroup

data class ReleaseGroupListItemModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String = "",
    override val disambiguation: String = "",
    override val primaryType: String? = null,
    override val secondaryTypes: List<String>? = null,
    val formattedArtistCredits: String? = null,
    val imageUrl: String? = null,
    override val visited: Boolean = false,
) : ListItemModel(), ReleaseGroup, Visitable
