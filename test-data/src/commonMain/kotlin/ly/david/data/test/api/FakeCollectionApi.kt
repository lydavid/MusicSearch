package ly.david.data.test.api

import ly.david.musicsearch.data.musicbrainz.api.BrowseCollectionsResponse
import ly.david.musicsearch.data.musicbrainz.api.CollectionApi

open class FakeCollectionApi : CollectionApi {
    override suspend fun addToCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: Set<String>,
        client: String,
    ) {
        // No-op.
    }

    override suspend fun deleteFromCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: Set<String>,
        client: String,
    ) {
        // No-op.
    }

    override suspend fun browseCollectionsByUser(
        username: String,
        limit: Int,
        offset: Int,
        include: String?,
    ): BrowseCollectionsResponse {
        return BrowseCollectionsResponse()
    }
}
