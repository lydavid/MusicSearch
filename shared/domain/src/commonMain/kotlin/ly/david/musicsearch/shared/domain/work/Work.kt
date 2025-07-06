package ly.david.musicsearch.shared.domain.work

import ly.david.musicsearch.shared.domain.NameWithDisambiguation

interface Work : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val languages: List<String>?
    val iswcs: List<String>?
}
