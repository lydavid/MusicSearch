package ly.david.musicsearch.data.core.listitem

import ly.david.musicsearch.data.core.releasegroup.ReleaseGroup
import ly.david.musicsearch.data.core.releasegroup.ReleaseGroupForListItem

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

// TODO: can we just move listitemmodel to core and have dao mapper map to it?
//  with our db as SSOT, it makes the most sense
fun ReleaseGroupForListItem.toReleaseGroupListItemModel() = ReleaseGroupListItemModel(
    id = id,
    name = name,
    firstReleaseDate = firstReleaseDate,
    disambiguation = disambiguation,
    primaryType = primaryType,
    secondaryTypes = secondaryTypes,
    formattedArtistCredits = formattedArtistCreditNames,
    imageUrl = thumbnailUrl
)
