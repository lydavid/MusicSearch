package ly.david.musicsearch.shared.domain.wikimedia

interface WikimediaRepository {

    /**
     * Given a [wikidataUrl] (e.g. https://www.wikidata.org/wiki/Q20019100),
     * return a Wikipedia extract for display.
     * Also, cache it for the given [mbid].
     *
     * If cached, we return this data together with the entity via SQL joins, rather than here.
     */
    suspend fun getWikipediaExtractFromNetwork(
        mbid: String,
        wikidataUrl: String,
    ): WikipediaExtract

    /**
     * Delete the cached Wikipedia extract for the given [mbid].
     */
    fun deleteWikipediaExtract(
        mbid: String,
    )
}
