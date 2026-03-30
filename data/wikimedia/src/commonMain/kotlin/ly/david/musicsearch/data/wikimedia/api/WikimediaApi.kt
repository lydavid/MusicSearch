package ly.david.musicsearch.data.wikimedia.api

import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

interface WikimediaApi {
    /**
     * Given a [wikidataId] (e.g. Q20019100) and an ISO-639 language tag (e.g. ja), return a Wikipedia extract.
     * See https://en.wikipedia.org/wiki/IETF_language_tag for specs.
     */
    suspend fun getWikipediaExtract(
        wikidataId: String,
        languageTag: String,
    ): WikipediaExtract
}
