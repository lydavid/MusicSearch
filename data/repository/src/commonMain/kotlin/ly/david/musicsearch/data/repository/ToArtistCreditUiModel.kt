package ly.david.musicsearch.data.repository

import ly.david.musicsearch.data.core.artist.ArtistCreditUiModel
import lydavidmusicsearchdatadatabase.Artist_credit_name

// TODO: remove
fun Artist_credit_name.toArtistCreditUiModel(): ArtistCreditUiModel =
    ArtistCreditUiModel(
        artistId = artist_id,
        name = name,
        joinPhrase = join_phrase
    )
