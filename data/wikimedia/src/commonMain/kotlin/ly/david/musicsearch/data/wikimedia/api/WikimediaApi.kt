package ly.david.musicsearch.data.wikimedia.api

import ly.david.musicsearch.shared.domain.image.ImageMetadata
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

    /**
     * Given a [wikidataId] (e.g. Q303), return urls to find the full-size and thumbnail image.
     * We try to get the "preferred" image if any, otherwise fall back to the first image.
     */
    suspend fun getWikimediaImageUrls(
        wikidataId: String,
    ): ImageMetadata
}
