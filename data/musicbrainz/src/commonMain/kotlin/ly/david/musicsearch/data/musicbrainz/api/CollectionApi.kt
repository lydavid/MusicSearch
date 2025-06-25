package ly.david.musicsearch.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.http.appendPathSegments
import ly.david.musicsearch.data.musicbrainz.SEARCH_BROWSE_LIMIT

interface CollectionApi {

    companion object {
        const val USER_COLLECTIONS = "user-collections"
    }

    suspend fun addToCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: Set<String>,
        client: String = "MusicSearch",
    )

    suspend fun deleteFromCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: Set<String>,
        client: String = "MusicSearch",
    )

    suspend fun browseCollectionsByUser(
        username: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
        include: String? = null,
    ): BrowseCollectionsResponse
}

interface CollectionApiImpl : CollectionApi {
    val httpClient: HttpClient

    override suspend fun addToCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: Set<String>,
        client: String,
    ) {
        httpClient.put {
            url {
                appendPathSegments(
                    "collection",
                    collectionId,
                    resourceUriPlural,
                    mbids.joinToString(";"),
                )
                parameter("client", client)
            }
        }
    }

    override suspend fun deleteFromCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: Set<String>,
        client: String,
    ) {
        httpClient.delete {
            url {
                appendPathSegments(
                    "collection",
                    collectionId,
                    resourceUriPlural,
                    mbids.joinToString(";"),
                )
                parameter("client", client)
            }
        }
    }

    override suspend fun browseCollectionsByUser(
        username: String,
        limit: Int,
        offset: Int,
        include: String?,
    ): BrowseCollectionsResponse {
        return httpClient.get {
            url {
                appendPathSegments("collection")
                parameter(
                    "editor",
                    username,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
                parameter(
                    "inc",
                    include,
                )
            }
        }.body()
    }
}
