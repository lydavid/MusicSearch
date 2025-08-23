package ly.david.musicsearch.data.listenbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface ListenBrainzApi {
    suspend fun getListensByUser(
        username: String,
        minTimestamp: Long? = null,
        maxTimestamp: Long? = null,
    ): ListensResponse
}

private const val BASE_URL = "https://api.listenbrainz.org/1/"

class ListenBrainzApiImpl(
    private val client: HttpClient,
) : ListenBrainzApi {
    override suspend fun getListensByUser(
        username: String,
        minTimestamp: Long?,
        maxTimestamp: Long?,
    ): ListensResponse {
        require(!(minTimestamp != null && maxTimestamp != null)) {
            "minTimestamp and maxTimestamp cannot both be set"
        }
        val url = buildString {
            append("$BASE_URL/user/$username/listens")
            // TODO: have a background task to load 1000 at a time if the user wants to load all
            append("?count=100")
            minTimestamp?.let { append("&min_ts=$it") }
            maxTimestamp?.let { append("&max_ts=$it") }
        }
        return client.get(url).body()
    }
}
