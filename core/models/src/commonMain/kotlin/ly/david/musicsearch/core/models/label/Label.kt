package ly.david.musicsearch.core.models.label

import ly.david.musicsearch.core.models.NameWithDisambiguation

interface Label : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val labelCode: Int?
}
