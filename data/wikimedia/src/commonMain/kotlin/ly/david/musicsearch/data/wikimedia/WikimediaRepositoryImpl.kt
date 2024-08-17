package ly.david.musicsearch.data.wikimedia

import ly.david.musicsearch.data.wikimedia.api.WikimediaApi
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

internal class WikimediaRepositoryImpl(
    private val api: WikimediaApi,
) : WikimediaRepository {
    override suspend fun getWikipediaExtractFromNetwork(
        mbid: String,
        wikidataUrl: String,
    ): WikipediaExtract {
        val wikidataId = wikidataUrl.split("/").last()
        return api.getWikipediaExtract(wikidataId)
    }

    override fun deleteWikipediaExtract(mbid: String) {
    }
}
