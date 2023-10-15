package ly.david.musicsearch.core.models.work

import ly.david.musicsearch.core.models.NameWithDisambiguation

interface Work : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val language: String?
    val iswcs: List<String>?
}
