package ly.david.mbjc.data.domain

import ly.david.mbjc.data.ReleaseGroup
import ly.david.mbjc.data.getDisplayNames
import ly.david.mbjc.data.network.ReleaseGroupMusicBrainzModel
import ly.david.mbjc.data.persistence.ReleaseGroupRoomModel
import ly.david.mbjc.data.persistence.ReleaseGroupArtistCreditRoomModel

/**
 * Representation of a [ReleaseGroup] for our UI.
 * This can be mapped from [ReleaseGroupRoomModel] or [ReleaseGroupMusicBrainzModel].
 */
data class ReleaseGroupUiModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String = "",
    override val disambiguation: String = "",
    override val primaryType: String? = null,
    override val secondaryTypes: List<String>? = null,

    // TODO: if we keep it as MusicBrainzArtistCredit, then we can deeplink to each artist's page from a dropdown
    //  if we join table with artists, we could also get the artist object
    val artistCredits: String = ""
): UiModel(), ReleaseGroup

fun ReleaseGroupMusicBrainzModel.toReleaseGroupUiModel(): ReleaseGroupUiModel {
    return ReleaseGroupUiModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,

        primaryType = primaryType,
        secondaryTypes = secondaryTypes,

        artistCredits = artistCredits.getDisplayNames()
    )
}

fun ReleaseGroupRoomModel.toReleaseGroupUiModel(releaseGroupArtistCreditRoomModels: List<ReleaseGroupArtistCreditRoomModel>): ReleaseGroupUiModel {
    return ReleaseGroupUiModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,

        primaryType = primaryType,
        secondaryTypes = secondaryTypes,

        artistCredits = releaseGroupArtistCreditRoomModels.getDisplayNames()
    )
}
