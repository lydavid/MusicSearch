package ly.david.data.domain.listitem

import ly.david.data.core.ReleaseGroup
import ly.david.data.core.ReleaseGroupForListItem
import ly.david.data.core.getDisplayNames
import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.data.room.releasegroup.ReleaseGroupRoomModel

// TODO: if this is in a non-android module, we don't have access to androidx.compose.runtime.Immutable
//  We could extract uimodel to data-android or app
/**
 * Representation of a [ReleaseGroup] for our UI.
 * This can be mapped from [ReleaseGroupRoomModel] or [ReleaseGroupMusicBrainzModel].
 */
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

fun ReleaseGroupRoomModel.toReleaseGroupListItemModel(): ReleaseGroupListItemModel {
    return ReleaseGroupListItemModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes
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
