package ly.david.data.domain.listitem

import ly.david.data.Genre
import ly.david.data.network.GenreMusicBrainzModel

data class GenreListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
) : Genre, ListItemModel()

internal fun GenreMusicBrainzModel.toGenreListItemModel() =
    GenreListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
    )
