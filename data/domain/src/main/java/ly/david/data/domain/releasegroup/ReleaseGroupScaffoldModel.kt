package ly.david.data.domain.releasegroup

import ly.david.data.ReleaseGroup
import ly.david.data.domain.artist.ArtistCreditUiModel
import ly.david.data.domain.artist.toArtistCreditUiModel
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import ly.david.data.room.releasegroup.ReleaseGroupWithAllData

data class ReleaseGroupScaffoldModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String = "",
    override val disambiguation: String = "",
    override val primaryType: String? = null,
    override val secondaryTypes: List<String>? = null,
    val artistCredits: List<ArtistCreditUiModel> = listOf(),
    val imageUrl: String? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ReleaseGroup

internal fun ReleaseGroupWithAllData.toReleaseGroupScaffoldModel(): ReleaseGroupScaffoldModel {
    return ReleaseGroupScaffoldModel(
        id = releaseGroup.id,
        name = releaseGroup.name,
        firstReleaseDate = releaseGroup.firstReleaseDate,
        disambiguation = releaseGroup.disambiguation,
        primaryType = releaseGroup.primaryType,
        secondaryTypes = releaseGroup.secondaryTypes,
        artistCredits = artistCreditNamesWithEntities.map {
            it.artistCreditNameRoomModel.toArtistCreditUiModel()
        },
        imageUrl = largeUrl,
        urls = urls.map { it.relation.toRelationListItemModel() },
    )
}
