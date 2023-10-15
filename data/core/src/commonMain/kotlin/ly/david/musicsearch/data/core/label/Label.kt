package ly.david.musicsearch.data.core.label

import ly.david.musicsearch.data.core.NameWithDisambiguation

interface Label : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val labelCode: Int?
}
