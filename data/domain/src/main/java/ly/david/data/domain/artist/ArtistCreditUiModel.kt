package ly.david.data.domain.artist

import ly.david.data.core.ArtistCreditName
import ly.david.data.musicbrainz.ArtistCreditMusicBrainzModel
import ly.david.data.room.artist.credit.ArtistCreditNameRoomModel
import lydavidmusicsearchdatadatabase.Artist_credit_name

// This will continue to use the term UiModel because they don't appear separately as a ListItem
data class ArtistCreditUiModel(
    val artistId: String,
    override val name: String,
    override val joinPhrase: String? = null,
) : ArtistCreditName

internal fun List<ArtistCreditMusicBrainzModel>?.toArtistCreditUiModels(): List<ArtistCreditUiModel> =
    this?.map { artistCredit ->
        ArtistCreditUiModel(
            artistId = artistCredit.artist.id,
            name = artistCredit.name,
            joinPhrase = artistCredit.joinPhrase
        )
    }.orEmpty()

internal fun ArtistCreditNameRoomModel.toArtistCreditUiModel(): ArtistCreditUiModel =
    ArtistCreditUiModel(
        artistId = artistId,
        name = name,
        joinPhrase = joinPhrase
    )

internal fun Artist_credit_name.toArtistCreditUiModel(): ArtistCreditUiModel =
    ArtistCreditUiModel(
        artistId = artist_id,
        name = name,
        joinPhrase = join_phrase
    )
