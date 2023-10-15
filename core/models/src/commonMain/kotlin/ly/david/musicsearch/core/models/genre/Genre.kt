package ly.david.musicsearch.core.models.genre

import ly.david.musicsearch.core.models.NameWithDisambiguation

interface Genre : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
}
