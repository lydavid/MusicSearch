package ly.david.data.domain

import ly.david.data.ReleaseGroup
import ly.david.data.getDisplayNames
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.persistence.releasegroup.ReleaseGroupForListItem
import ly.david.data.persistence.releasegroup.ReleaseGroupRoomModel

// TODO: if this is in a non-android module, we don't have access to androidx.compose.runtime.Immutable
//  We could extract uimodel to data-android or app
/**
 * Representation of a [ReleaseGroup] for our UI.
 * This can be mapped from [ReleaseGroupRoomModel] or [ReleaseGroupMusicBrainzModel].
 */
//@Immutable
data class ReleaseGroupListItemModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String = "",
    override val disambiguation: String = "",
    override val primaryType: String? = null,

    // Lists are considered unstable by Compose.
    // Since this is just a list of primitives, we will mark this class immutable.
    override val secondaryTypes: List<String>? = null,

    val formattedArtistCredits: String? = null,
    val hasCoverArt: Boolean? = null,
    val coverArtUrl: String? = null,
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
        hasCoverArt = null,
        coverArtUrl = null
    )
}

fun ReleaseGroupRoomModel.toReleaseGroupListItemModel(): ReleaseGroupListItemModel {
    return ReleaseGroupListItemModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes,
        hasCoverArt = hasCoverArt,
        coverArtUrl = coverArtUrl
    )
}

fun ReleaseGroupForListItem.toReleaseGroupListItemModel(): ReleaseGroupListItemModel {
    return ReleaseGroupListItemModel(
        id = releaseGroup.id,
        name = releaseGroup.name,
        firstReleaseDate = releaseGroup.firstReleaseDate,
        disambiguation = releaseGroup.disambiguation,
        primaryType = releaseGroup.primaryType,
        secondaryTypes = releaseGroup.secondaryTypes,
        formattedArtistCredits = artistCreditNames,
        hasCoverArt = releaseGroup.hasCoverArt,
        coverArtUrl = releaseGroup.coverArtUrl
    )
}
