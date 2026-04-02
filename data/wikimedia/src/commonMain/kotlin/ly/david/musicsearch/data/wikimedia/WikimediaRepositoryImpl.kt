package ly.david.musicsearch.data.wikimedia

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.wikimedia.api.WikimediaApi
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.wikimedia.MbidWikipediaDao
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

private const val WIKIDATA_URL = "www.wikidata.org/wiki/"

internal class WikimediaRepositoryImpl(
    private val wikimediaApi: WikimediaApi,
    private val mbidWikipediaDao: MbidWikipediaDao,
    private val logger: Logger,
) : WikimediaRepository {

    override suspend fun getWikipediaExtract(
        mbid: String,
        urls: List<RelationListItemModel>,
        languageTag: String,
        forceRefresh: Boolean,
    ): Result<WikipediaExtract> {
        if (forceRefresh) {
            mbidWikipediaDao.deleteByIdAndTag(
                mbid = mbid,
                languageTag = languageTag,
            )
        }

        val cachedExtract = mbidWikipediaDao.get(mbid, languageTag)
        return if (cachedExtract == null) {
            getWikipediaExtractFromNetwork(
                mbid = mbid,
                urls = urls,
                languageTag = languageTag,
            )
        } else {
            Result.success(cachedExtract)
        }
    }

    private suspend fun getWikipediaExtractFromNetwork(
        mbid: String,
        urls: List<RelationListItemModel>,
        languageTag: String,
    ): Result<WikipediaExtract> {
        return try {
            val wikidataId = getWikimediaId(urls) ?: return Result.success(WikipediaExtract())
            val wikipediaExtract = wikimediaApi.getWikipediaExtract(
                wikidataId = wikidataId,
                languageTag = languageTag,
            )
            cache(
                mbid = mbid,
                languageTag = languageTag,
                wikipediaExtract = wikipediaExtract,
            )
            Result.success(wikipediaExtract)
        } catch (ex: HandledException) {
            if (ex.errorResolution != ErrorResolution.None) {
                logger.e(ex)
            }
            Result.failure(ex)
        }
    }

    private fun getWikimediaId(
        urls: List<RelationListItemModel>,
    ): String? {
        val wikidataUrl = urls.firstOrNull { it.name.contains(WIKIDATA_URL) }?.name
        val wikidataId = wikidataUrl?.split("/")?.last()
        return wikidataId
    }

    private fun cache(
        mbid: String,
        languageTag: String,
        wikipediaExtract: WikipediaExtract,
    ) {
        mbidWikipediaDao.save(
            mbid = mbid,
            languageTag = languageTag,
            wikipediaExtract = wikipediaExtract,
        )
    }

    override suspend fun getWikimediaImage(
        urls: List<RelationListItemModel>,
    ): ImageMetadata {
        val wikidataId = getWikimediaId(urls) ?: return ImageMetadata()
        return wikimediaApi.getWikimediaImageUrls(
            wikidataId = wikidataId,
        )
    }
}
