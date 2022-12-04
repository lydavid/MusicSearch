package ly.david.data.domain

import ly.david.data.ArtistCreditName
import ly.david.data.network.ArtistCreditMusicBrainzModel
import ly.david.data.persistence.artist.credit.ArtistCreditNameRoomModel

// This will continue to use the term UiModel because they don't appear separately as a ListItem
data class ArtistCreditUiModel(
    val artistId: String,
    override val name: String,
    override val joinPhrase: String? = null
) : ArtistCreditName

fun List<ArtistCreditMusicBrainzModel>?.toArtistCreditUiModels(): List<ArtistCreditUiModel> =
    this?.map { artistCredit ->
        ArtistCreditUiModel(
            artistId = artistCredit.artist.id,
            name = artistCredit.name,
            joinPhrase = artistCredit.joinPhrase
        )
    }.orEmpty()

fun ArtistCreditNameRoomModel.toArtistCreditUiModel(): ArtistCreditUiModel =
    ArtistCreditUiModel(
        artistId = artistId,
        name = name,
        joinPhrase = joinPhrase
    )
