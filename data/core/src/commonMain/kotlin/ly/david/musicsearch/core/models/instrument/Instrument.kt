package ly.david.musicsearch.core.models.instrument

import ly.david.musicsearch.core.models.NameWithDisambiguation

interface Instrument : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val description: String?
    val type: String?
}
