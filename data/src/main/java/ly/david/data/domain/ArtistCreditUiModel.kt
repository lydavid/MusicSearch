package ly.david.data.domain

import ly.david.data.ArtistCredit
import ly.david.data.network.ArtistCreditMusicBrainzModel

data class ArtistCreditUiModel(
    val position: Int,
    val artistId: String,
    override val name: String,
    override val joinPhrase: String? = null
) : ArtistCredit

fun List<ArtistCreditMusicBrainzModel>?.toUiModels(): List<ArtistCreditUiModel> =
    this?.mapIndexed { index, artistCredit ->
        ArtistCreditUiModel(
            position = index,
            artistId = artistCredit.artist.id,
            name = artistCredit.name,
            joinPhrase = artistCredit.joinPhrase
        )
    }.orEmpty()
