package ly.david.musicsearch.shared.domain.wikimedia

import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel

interface WikimediaRepository {

    /**
     * Given [urls] that may include a Wikidata url (e.g. https://www.wikidata.org/wiki/Q20019100),
     * return a Wikipedia extract for display.
     * Also, cache it for the given [mbid].
     */
    suspend fun getWikipediaExtract(
        mbid: String,
        urls: List<RelationListItemModel>,
        forceRefresh: Boolean,
    ): Result<WikipediaExtract>
}
