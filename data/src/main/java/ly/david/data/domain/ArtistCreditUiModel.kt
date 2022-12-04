package ly.david.data.domain

import ly.david.data.ArtistCreditName
import ly.david.data.network.ArtistCreditMusicBrainzModel
import ly.david.data.persistence.artist.credit.ArtistCreditNameRoomModel

// This will continue to use the term UiModel because these are not meant to appear as list items
// But they are list items when appearing in subtitle...
data class ArtistCreditUiModel(
    val artistId: String,
    override val name: String,
    override val joinPhrase: String? = null
) : ArtistCreditName

fun List<ArtistCreditMusicBrainzModel>?.toUiModels(): List<ArtistCreditUiModel> =
    this?.map { artistCredit ->
        ArtistCreditUiModel(
            artistId = artistCredit.artist.id,
            name = artistCredit.name,
            joinPhrase = artistCredit.joinPhrase
        )
    }.orEmpty()

fun ArtistCreditNameRoomModel.toUiModel(): ArtistCreditUiModel =
    ArtistCreditUiModel(
        artistId = artistId,
        name = name,
        joinPhrase = joinPhrase
    )
