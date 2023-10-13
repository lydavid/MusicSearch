package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.releasegroup.ReleaseGroup

data class ReleaseGroupListItemModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String = "",
    override val disambiguation: String = "",
    override val primaryType: String? = null,
    override val secondaryTypes: List<String>? = null,
    val formattedArtistCredits: String? = null,
    val imageUrl: String? = null,
) : ListItemModel(), ReleaseGroup
