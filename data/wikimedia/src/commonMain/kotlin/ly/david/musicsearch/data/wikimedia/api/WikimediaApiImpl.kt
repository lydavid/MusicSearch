package ly.david.musicsearch.data.wikimedia.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

private const val WIKIDATA_BASE_URL = "https://www.wikidata.org/w/api.php"

internal class WikimediaApiImpl(
    private val client: HttpClient,
    private val logger: Logger,
) : WikimediaApi {

    override suspend fun getWikipediaExtract(
        wikidataId: String,
        languageTag: String,
    ): WikipediaExtract {
        // 1. Get Wikipedia url using the Wikidata ID
        val wikidataUrl = WIKIDATA_BASE_URL +
            "?action=wbgetentities" +
            "&format=json" +
            "&props=sitelinks%2Furls" +
            "&ids=$wikidataId"
        val wikidataResponse: HttpResponse = client.get(wikidataUrl)
        val wikidataJson = Json.parseToJsonElement(wikidataResponse.body<String>()).jsonObject

        val siteLinks = wikidataJson["entities"]
            ?.jsonObject?.get(wikidataId)
            ?.jsonObject?.get("sitelinks")
        val preferredLanguageWiki = siteLinks?.jsonObject?.get("${languageTag}wiki")
        val enWiki = siteLinks?.jsonObject?.get("enwiki")

        val wikipediaUrl: String? = preferredLanguageWiki
            ?.jsonObject?.get("url")
            ?.jsonPrimitive?.content ?: enWiki
            ?.jsonObject?.get("url")
            ?.jsonPrimitive?.content

        if (wikipediaUrl == null) {
            logger.d("Wikipedia url not found for Wikidata ID: $wikidataId")
            return WikipediaExtract()
        }

        // 2. Get Wikipedia extract using the url by transforming it to the api endpoint
        val parameters = "action=query" +
            "&format=json" +
            "&prop=extracts" +
            "&exintro" + // Return only content before the first section.
            "&explaintext" + // Return extracts as plain text instead of limited HTML.
            "&titles="
        val wikipediaApiUrl = wikipediaUrl.replace("wiki/", "w/api.php?$parameters")
        val wikipediaResponse: HttpResponse = client.get(wikipediaApiUrl)
        val wikipediaJson = Json.parseToJsonElement(wikipediaResponse.body<String>()).jsonObject

        val pageId = wikipediaJson["query"]
            ?.jsonObject?.get("pages")
            ?.jsonObject?.keys
            ?.firstOrNull()

        val extract = pageId?.let { id ->
            wikipediaJson["query"]
                ?.jsonObject?.get("pages")
                ?.jsonObject?.get(id)
                ?.jsonObject?.get("extract")
                ?.jsonPrimitive?.content
        }

        return WikipediaExtract(
            extract = extract.orEmpty(),
            wikipediaUrl = wikipediaUrl,
        )
    }

    override suspend fun getWikimediaImageUrls(
        wikidataId: String,
    ): ImageMetadata {
        val wikidataUrl = WIKIDATA_BASE_URL +
            "?action=wbgetclaims" +
            "&format=json" +
            "&property=P18" +
            "&entity=$wikidataId"
        val wikidataResponse: HttpResponse = client.get(wikidataUrl)
        val wikidataJson = Json.parseToJsonElement(wikidataResponse.body<String>()).jsonObject

        val imageObjects: JsonArray? = wikidataJson["claims"]
            ?.jsonObject?.get("P18")
            ?.jsonArray
        val preferredImageObject = imageObjects?.firstOrNull {
            it.jsonObject["rank"]?.jsonPrimitive?.content == "preferred"
        }
        val imageObject = preferredImageObject ?: imageObjects?.firstOrNull()
        val imageName = imageObject
            ?.jsonObject?.get("mainsnak")
            ?.jsonObject?.get("datavalue")
            ?.jsonObject?.get("value")
            ?.jsonPrimitive?.content

        if (imageName == null) {
            return ImageMetadata()
        }

        val fullSizeUrl = "https://commons.wikimedia.org/w/index.php?title=Special:Redirect/file/$imageName"
        val thumbnailUrl = "$fullSizeUrl&width=64"

        return ImageMetadata(
            thumbnailUrl = thumbnailUrl,
            largeUrl = fullSizeUrl,
        )
    }
}
