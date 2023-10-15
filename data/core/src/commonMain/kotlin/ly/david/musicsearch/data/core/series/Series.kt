package ly.david.musicsearch.data.core.series

import ly.david.musicsearch.data.core.NameWithDisambiguation

interface Series : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
}
