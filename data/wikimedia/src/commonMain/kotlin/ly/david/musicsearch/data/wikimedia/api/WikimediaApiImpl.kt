package ly.david.musicsearch.data.wikimedia.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract

private const val WIKIDATA_BASE_URL = "https://www.wikidata.org/w/api.php"
private const val EN_WIKIPEDIA_BASE_URL = "https://en.wikipedia.org/w/api.php"

internal class WikimediaApiImpl(
    private val client: HttpClient,
    private val logger: Logger,
) : WikimediaApi {

    override suspend fun getWikipediaExtract(wikidataId: String): WikipediaExtract {
        // 1. Get Wikipedia page title using the Wikidata ID
        val wikidataUrl = WIKIDATA_BASE_URL +
            "?action=wbgetentities" +
            "&format=json" +
            "&props=sitelinks%2Furls" +
            "&sitefilter=enwiki" +
            "&ids=$wikidataId"
        val wikidataResponse: HttpResponse = client.get(wikidataUrl)
        val wikidataJson = Json.parseToJsonElement(wikidataResponse.body<String>()).jsonObject

        val enWiki: JsonElement? = wikidataJson["entities"]
            ?.jsonObject?.get(wikidataId)
            ?.jsonObject?.get("sitelinks")
            ?.jsonObject?.get("enwiki")

        val pageTitle: String? = enWiki
            ?.jsonObject?.get("title")
            ?.jsonPrimitive?.content

        if (pageTitle == null) {
            logger.d("Wikipedia page title not found for Wikidata ID: $wikidataId")
            return WikipediaExtract()
        }

        val wikipediaUrl = enWiki
            .jsonObject["url"]
            ?.jsonPrimitive?.content

        // 2. Get Wikipedia extract using the page title
        // https://en.wikipedia.org/w/api.php?action=help&modules=query
        val wikipediaApiUrl = EN_WIKIPEDIA_BASE_URL +
            "?action=query" +
            "&format=json" +
            "&prop=extracts" +
            "&exintro" + // Return only content before the first section.
            "&explaintext" + // Return extracts as plain text instead of limited HTML.
            "&titles=$pageTitle"
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
            wikipediaUrl = wikipediaUrl.orEmpty(),
        )
    }
}
