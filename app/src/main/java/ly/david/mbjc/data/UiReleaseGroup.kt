package ly.david.mbjc.data

import ly.david.mbjc.data.network.MusicBrainzReleaseGroup
import ly.david.mbjc.data.persistence.RoomReleaseGroup
import ly.david.mbjc.data.persistence.RoomReleaseGroupArtistCredit

/**
 * Representation of a [ReleaseGroup] for our UI.
 * This can be mapped from [RoomReleaseGroup] or [MusicBrainzReleaseGroup].
 */
data class UiReleaseGroup(
    override val id: String,
    override val title: String,
    override val firstReleaseDate: String,
    override val disambiguation: String,
    override val primaryType: String?,
    override val secondaryTypes: List<String>?,

    // TODO: if we keep it as MusicBrainzArtistCredit, then we can deeplink to each artist's page from a dropdown
    //  if we join table with artists, we could also get the artist object
    val artistCredits: String
): ReleaseGroup

fun MusicBrainzReleaseGroup.toUiReleaseGroup(): UiReleaseGroup {
    return UiReleaseGroup(
        id = id,
        title = title,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,

        primaryType = primaryType,
        secondaryTypes = secondaryTypes,

        artistCredits = artistCredits.getDisplayNames()
    )
}

fun RoomReleaseGroup.toUiReleaseGroup(roomReleaseGroupArtistCredits: List<RoomReleaseGroupArtistCredit>): UiReleaseGroup {
    return UiReleaseGroup(
        id = id,
        title = title,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,

        primaryType = primaryType,
        secondaryTypes = secondaryTypes,

        artistCredits = roomReleaseGroupArtistCredits.getDisplayNames()
    )
}
