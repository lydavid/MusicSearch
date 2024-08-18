package ly.david.musicsearch.data.wikimedia.api

import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

interface WikimediaApi {
    /**
     * Given a [wikidataId] (e.g. Q20019100), return a Wikipedia extract.
     */
    suspend fun getWikipediaExtract(wikidataId: String): WikipediaExtract
}
