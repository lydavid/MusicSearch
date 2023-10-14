package ly.david.musicsearch.data.core.artist

// This will continue to use the term UiModel because they don't appear separately as a ListItem
data class ArtistCreditUiModel(
    val artistId: String,
    override val name: String,
    override val joinPhrase: String? = null,
) : ArtistCreditName
