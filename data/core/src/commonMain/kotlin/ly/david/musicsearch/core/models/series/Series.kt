package ly.david.musicsearch.core.models.series

import ly.david.musicsearch.core.models.NameWithDisambiguation

interface Series : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
}
