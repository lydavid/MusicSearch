package ly.david.musicsearch.data.wikimedia

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.wikimedia.api.WikimediaApi
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.wikimedia.MbidWikipediaDao
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

internal class WikimediaRepositoryImpl(
    private val wikimediaApi: WikimediaApi,
    private val mbidWikipediaDao: MbidWikipediaDao,
    private val logger: Logger,
) : WikimediaRepository {

    override suspend fun getWikipediaExtract(
        mbid: String,
        urls: List<RelationListItemModel>,
        forceRefresh: Boolean,
    ): Result<WikipediaExtract> {
        if (forceRefresh) {
            mbidWikipediaDao.deleteById(mbid)
        }

        val cachedExtract = mbidWikipediaDao.get(mbid)
        return if (cachedExtract == null) {
            getWikipediaExtractFromNetwork(
                mbid = mbid,
                urls = urls,
            )
        } else {
            Result.success(cachedExtract)
        }
    }

    private suspend fun getWikipediaExtractFromNetwork(
        mbid: String,
        urls: List<RelationListItemModel>,
    ): Result<WikipediaExtract> {
        return try {
            val wikidataUrl =
                urls.firstOrNull { it.name.contains("www.wikidata.org/wiki/") }?.name
                    ?: return Result.success(WikipediaExtract())
            val wikidataId = wikidataUrl.split("/").last()
            val wikipediaExtract = wikimediaApi.getWikipediaExtract(wikidataId = wikidataId)
            cache(mbid, wikipediaExtract)
            Result.success(wikipediaExtract)
        } catch (ex: HandledException) {
            if (ex.errorResolution != ErrorResolution.None) {
                logger.e(ex)
            }
            Result.failure(ex)
        }
    }

    private fun cache(
        mbid: String,
        wikipediaExtract: WikipediaExtract,
    ) {
        mbidWikipediaDao.save(
            mbid = mbid,
            wikipediaExtract = wikipediaExtract,
        )
    }
}
