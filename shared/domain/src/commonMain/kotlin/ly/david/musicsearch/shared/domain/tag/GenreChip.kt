package ly.david.musicsearch.shared.domain.tag

import ly.david.musicsearch.shared.domain.genre.Genre
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation

data class GenreChip(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val count: Int,
) : Genre, GenreOrTag {
    override val fullName: String
        get() = getNameWithDisambiguation()
}
