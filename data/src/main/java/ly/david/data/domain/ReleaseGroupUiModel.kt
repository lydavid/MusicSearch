package ly.david.data.domain

import ly.david.data.ReleaseGroup
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.persistence.releasegroup.ReleaseGroupRoomModel
import ly.david.data.persistence.releasegroup.ReleaseGroupWithArtistCredits

// TODO: if this is in a non-android module, we can't mark it Immutable
//  We could extract uimodel to data-android or app
/**
 * Representation of a [ReleaseGroup] for our UI.
 * This can be mapped from [ReleaseGroupRoomModel] or [ReleaseGroupMusicBrainzModel].
 */
//@Immutable
data class ReleaseGroupUiModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String = "",
    override val disambiguation: String = "",
    override val primaryType: String? = null,

    // Lists are considered unstable by Compose.
    // Since this is just a list of primitives, we will mark this class immutable.
    override val secondaryTypes: List<String>? = null,

    val artistCredits: List<ArtistCreditUiModel> = listOf()
) : UiModel(), ReleaseGroup

fun ReleaseGroupMusicBrainzModel.toUiModel(): ReleaseGroupUiModel {
    return ReleaseGroupUiModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,

        primaryType = primaryType,
        secondaryTypes = secondaryTypes,

        artistCredits = artistCredits.toUiModels()
    )
}

fun ReleaseGroupRoomModel.toUiModel(): ReleaseGroupUiModel {
    return ReleaseGroupUiModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes,
    )
}

fun ReleaseGroupWithArtistCredits.toUiModel(): ReleaseGroupUiModel {
    return ReleaseGroupUiModel(
        id = releaseGroup.id,
        name = releaseGroup.name,
        firstReleaseDate = releaseGroup.firstReleaseDate,
        disambiguation = releaseGroup.disambiguation,

        primaryType = releaseGroup.primaryType,
        secondaryTypes = releaseGroup.secondaryTypes,

        artistCredits = artistCreditNamesWithResources.map {
            it.artistCreditNameRoomModel.toUiModel()
        }
    )
}
