package ly.david.musicsearch.data.wikimedia

import ly.david.musicsearch.data.wikimedia.api.WikimediaApi
import ly.david.musicsearch.shared.domain.wikimedia.MbidWikipediaDao
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

internal class WikimediaRepositoryImpl(
    private val api: WikimediaApi,
    private val mbidWikipediaDao: MbidWikipediaDao,
) : WikimediaRepository {
    override suspend fun getWikipediaExtractFromNetwork(
        mbid: String,
        wikidataUrl: String,
    ): WikipediaExtract {
        val wikidataId = wikidataUrl.split("/").last()
        val wikipediaExtract = api.getWikipediaExtract(wikidataId = wikidataId)
        mbidWikipediaDao.save(mbid, wikipediaExtract)
        return wikipediaExtract
    }

    override fun deleteWikipediaExtract(mbid: String) {
        mbidWikipediaDao.deleteById(mbid)
    }
}
