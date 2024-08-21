package ly.david.musicsearch.shared.domain.wikimedia

interface MbidWikipediaDao {
    fun save(
        mbid: String,
        wikipediaExtract: WikipediaExtract,
    )

    fun get(mbid: String): WikipediaExtract?

    fun deleteById(mbid: String)
}
