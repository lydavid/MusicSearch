package ly.david.data.domain

import ly.david.data.ArtistCredit
import ly.david.data.network.ArtistCreditMusicBrainzModel
import ly.david.data.persistence.artist.credit.ArtistCreditNameRoomModel

// TODO: position unused since we fetch these ordered
data class ArtistCreditUiModel(
    val artistId: String,
    override val name: String,
    override val joinPhrase: String? = null
) : ArtistCredit

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
