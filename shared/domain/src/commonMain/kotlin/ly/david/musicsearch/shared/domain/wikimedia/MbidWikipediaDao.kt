package ly.david.musicsearch.shared.domain.wikimedia

interface MbidWikipediaDao {

    /**
     * Rather than derive [languageTag] from [WikipediaExtract.wikipediaUrl],
     * we want to save the one from the user's system, so that we don't keep trying to fetch it from network.
     * It's also possible that there were no urls, in which case we would want to store
     * their tag rather than an empty string.
     */
    fun save(
        mbid: String,
        languageTag: String,
        wikipediaExtract: WikipediaExtract,
    )

    fun get(
        mbid: String,
        languageTag: String,
    ): WikipediaExtract?

    fun deleteById(
        mbid: String,
        languageTag: String,
    )
}
