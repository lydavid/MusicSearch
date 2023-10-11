package ly.david.musicsearch.domain.listitem

import ly.david.data.core.artist.getDisplayNames
import ly.david.data.core.releasegroup.ReleaseGroup
import ly.david.data.core.releasegroup.ReleaseGroupForListItem
import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel

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

fun ReleaseGroupMusicBrainzModel.toReleaseGroupListItemModel(): ReleaseGroupListItemModel {
    return ReleaseGroupListItemModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes,
        formattedArtistCredits = artistCredits.getDisplayNames(),
    )
}

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
