package ly.david.musicsearch.data.core

interface Work : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val language: String?
    val iswcs: List<String>?
}
