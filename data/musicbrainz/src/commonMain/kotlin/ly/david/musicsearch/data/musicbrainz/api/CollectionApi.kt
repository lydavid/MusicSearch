package ly.david.musicsearch.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.http.appendPathSegments

interface CollectionApi {

    companion object {
        const val USER_COLLECTIONS = "user-collections"
    }

    suspend fun uploadToCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: String,
        client: String = "MusicSearch",
    )

    suspend fun deleteFromCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: String,
        client: String = "MusicSearch",
    )
}

interface CollectionApiImpl : CollectionApi {
    val httpClient: HttpClient

    override suspend fun uploadToCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: String,
        client: String,
    ) {
        httpClient.put {
            url {
                appendPathSegments("collection", collectionId, resourceUriPlural, mbids)
                parameter("client", client)
            }
        }
    }

    override suspend fun deleteFromCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: String,
        client: String,
    ) {
        httpClient.delete {
            url {
                appendPathSegments("collection", collectionId, resourceUriPlural, mbids)
                parameter("client", client)
            }
        }
    }
}
