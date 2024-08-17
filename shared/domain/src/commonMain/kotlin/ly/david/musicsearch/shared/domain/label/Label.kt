package ly.david.musicsearch.shared.domain.label

import ly.david.musicsearch.shared.domain.NameWithDisambiguation

interface Label : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val labelCode: Int?
}
