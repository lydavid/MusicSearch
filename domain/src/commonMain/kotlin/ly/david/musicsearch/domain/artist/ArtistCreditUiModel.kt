package ly.david.musicsearch.domain.artist

import ly.david.musicsearch.data.core.artist.ArtistCreditName
import lydavidmusicsearchdatadatabase.Artist_credit_name

// This will continue to use the term UiModel because they don't appear separately as a ListItem
data class ArtistCreditUiModel(
    val artistId: String,
    override val name: String,
    override val joinPhrase: String? = null,
) : ArtistCreditName

internal fun Artist_credit_name.toArtistCreditUiModel(): ArtistCreditUiModel =
    ArtistCreditUiModel(
        artistId = artist_id,
        name = name,
        joinPhrase = join_phrase
    )
