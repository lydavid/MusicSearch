package ly.david.data.domain

import ly.david.data.ReleaseGroup
import ly.david.data.persistence.releasegroup.ReleaseGroupWithAllData

data class ReleaseGroupScaffoldModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String = "",
    override val disambiguation: String = "",
    override val primaryType: String? = null,
    override val secondaryTypes: List<String>? = null,
    val artistCredits: List<ArtistCreditUiModel> = listOf(),
    val coverArtPath: String? = null,
) : ReleaseGroup

fun ReleaseGroupWithAllData.toReleaseGroupScaffoldModel(): ReleaseGroupScaffoldModel {
    return ReleaseGroupScaffoldModel(
        id = releaseGroup.id,
        name = releaseGroup.name,
        firstReleaseDate = releaseGroup.firstReleaseDate,
        disambiguation = releaseGroup.disambiguation,
        primaryType = releaseGroup.primaryType,
        secondaryTypes = releaseGroup.secondaryTypes,
        artistCredits = artistCreditNamesWithResources.map {
            it.artistCreditNameRoomModel.toArtistCreditUiModel()
        },
        coverArtPath = releaseGroup.coverArtPath
    )
}
