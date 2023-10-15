package ly.david.musicsearch.data.core.instrument

import ly.david.musicsearch.data.core.NameWithDisambiguation

interface Instrument : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val description: String?
    val type: String?
}
