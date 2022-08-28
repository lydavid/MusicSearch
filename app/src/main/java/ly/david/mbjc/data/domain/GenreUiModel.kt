package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Genre
import ly.david.mbjc.data.network.GenreMusicBrainzModel

internal data class GenreUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
) : Genre, UiModel()

internal fun GenreMusicBrainzModel.toGenreUiModel() =
    GenreUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
    )
