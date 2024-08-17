package ly.david.musicsearch.shared.domain.genre

import ly.david.musicsearch.shared.domain.NameWithDisambiguation

interface Genre : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
}
