package ly.david.mbjc.data.domain

import androidx.compose.runtime.Immutable
import ly.david.mbjc.data.ReleaseGroup
import ly.david.mbjc.data.network.ReleaseGroupMusicBrainzModel
import ly.david.mbjc.data.network.getRoomReleaseGroupArtistCredit
import ly.david.mbjc.data.persistence.artist.ReleaseGroupArtistCreditRoomModel
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupRoomModel

/**
 * Representation of a [ReleaseGroup] for our UI.
 * This can be mapped from [ReleaseGroupRoomModel] or [ReleaseGroupMusicBrainzModel].
 */
@Immutable
internal data class ReleaseGroupUiModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String = "",
    override val disambiguation: String = "",
    override val primaryType: String? = null,

    // Lists are considered unstable by Compose.
    // Since this is just a list of primitives, we will mark this class immutable.
    override val secondaryTypes: List<String>? = null,

    val artistCredits: List<ReleaseGroupArtistCreditRoomModel> = listOf()
): UiModel(), ReleaseGroup

internal fun ReleaseGroupMusicBrainzModel.toReleaseGroupUiModel(): ReleaseGroupUiModel {
    return ReleaseGroupUiModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,

        primaryType = primaryType,
        secondaryTypes = secondaryTypes,

        artistCredits = getRoomReleaseGroupArtistCredit()
    )
}

internal fun ReleaseGroupRoomModel.toReleaseGroupUiModel(releaseGroupArtistCreditRoomModels: List<ReleaseGroupArtistCreditRoomModel>): ReleaseGroupUiModel {
    return ReleaseGroupUiModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,

        primaryType = primaryType,
        secondaryTypes = secondaryTypes,

        artistCredits = releaseGroupArtistCreditRoomModels
    )
}
