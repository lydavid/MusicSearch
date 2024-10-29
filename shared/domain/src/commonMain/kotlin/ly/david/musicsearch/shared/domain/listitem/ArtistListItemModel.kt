package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.artist.Artist

data class ArtistListItemModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val gender: String? = null,
    val countryCode: String? = null,
    val lifeSpan: LifeSpanUiModel = LifeSpanUiModel(),
    val imageUrl: String? = null,
    override val visited: Boolean = false,
) : ListItemModel(), Artist, Visitable
