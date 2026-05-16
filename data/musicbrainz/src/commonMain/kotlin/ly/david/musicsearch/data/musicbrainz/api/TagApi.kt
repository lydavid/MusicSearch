package ly.david.musicsearch.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import ly.david.musicsearch.shared.domain.APP_NAME
import ly.david.musicsearch.shared.domain.tag.TagVote

internal const val TAG = "tag"

interface TagApi {
    suspend fun postUserTags(
        resourceUriSingular: String,
        mbid: String,
        tags: List<TagVote>,
    )
}

interface TagApiImpl : TagApi {
    val httpClient: HttpClient

    override suspend fun postUserTags(
        resourceUriSingular: String,
        mbid: String,
        tags: List<TagVote>,
    ) {
        val xmlBody = buildString {
            appendLine("""<metadata xmlns="http://musicbrainz.org/ns/mmd-2.0#">""")
            appendLine("    <$resourceUriSingular-list>")
            appendLine("""        <$resourceUriSingular id="$mbid">""")
            appendLine("            <user-tag-list>")
            for (tag in tags) {
                appendLine("                <user-tag vote=\"${tag.voteType}\"><name>${tag.name}</name></user-tag>")
            }
            appendLine("            </user-tag-list>")
            appendLine("        </$resourceUriSingular>")
            appendLine("    </$resourceUriSingular-list>")
            appendLine("</metadata>")
        }

        httpClient.post {
            url {
                appendPathSegments(TAG)
                parameter("client", APP_NAME)
            }
            contentType(ContentType.Companion.parse("application/xml; charset=utf-8"))
            setBody(xmlBody)
        }
    }
}
