package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.GenreMusicBrainzModel
import ly.david.musicsearch.data.core.listitem.GenreListItemModel

internal fun GenreMusicBrainzModel.toGenreListItemModel() =
    GenreListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
    )
