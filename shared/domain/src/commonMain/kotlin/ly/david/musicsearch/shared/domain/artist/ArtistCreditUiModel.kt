package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize

// This will continue to use the term UiModel because they don't appear separately as a ListItem
@Parcelize
data class ArtistCreditUiModel(
    val artistId: String,
    override val name: String,
    override val joinPhrase: String? = null,
) : ArtistCreditName, CommonParcelable
