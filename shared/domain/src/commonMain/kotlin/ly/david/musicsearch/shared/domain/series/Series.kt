package ly.david.musicsearch.shared.domain.series

import ly.david.musicsearch.shared.domain.NameWithDisambiguation

interface Series : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
}
