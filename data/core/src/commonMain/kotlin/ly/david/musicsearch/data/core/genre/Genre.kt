package ly.david.musicsearch.data.core.genre

import ly.david.musicsearch.data.core.NameWithDisambiguation

interface Genre : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
}
