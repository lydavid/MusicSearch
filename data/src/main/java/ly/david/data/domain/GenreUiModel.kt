package ly.david.data.domain

import ly.david.data.Genre
import ly.david.data.network.GenreMusicBrainzModel

data class GenreUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
) : Genre, UiModel()

internal fun GenreMusicBrainzModel.toInstrumentUiModel() =
    GenreUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
    )
