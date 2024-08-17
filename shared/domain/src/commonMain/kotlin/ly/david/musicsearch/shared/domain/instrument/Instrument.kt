package ly.david.musicsearch.shared.domain.instrument

import ly.david.musicsearch.shared.domain.NameWithDisambiguation

interface Instrument : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val description: String?
    val type: String?
}
