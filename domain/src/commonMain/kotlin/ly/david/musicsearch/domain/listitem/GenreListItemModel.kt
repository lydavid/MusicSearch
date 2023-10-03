package ly.david.musicsearch.domain.listitem

import ly.david.data.core.Genre
import ly.david.data.musicbrainz.GenreMusicBrainzModel

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
