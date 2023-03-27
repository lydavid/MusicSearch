package ly.david.data.network.api

import com.squareup.moshi.Json
import ly.david.data.network.CollectionMusicBrainzModel
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CollectionApi {

    companion object {
        const val USER_COLLECTIONS = "user-collections"
    }

    @GET("collection")
    suspend fun browseCollectionsByUser(
        @Query("editor") username: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
        @Query("inc") include: String? = null
    ): BrowseCollectionsResponse

    @PUT("collection/{collectionId}/{resourceUriPlural}/{mbids}")
    suspend fun uploadToCollection(
        @Path("collectionId") collectionId: String,
        @Path("resourceUriPlural") resourceUriPlural: String,
        @Path("mbids") mbids: String,
        @Query("client") client: String = "MusicSearch"
    )
}

data class BrowseCollectionsResponse(
    @Json(name = "collection-count") override val count: Int,
    @Json(name = "collection-offset") override val offset: Int,
    @Json(name = "collections") override val musicBrainzModels: List<CollectionMusicBrainzModel>
) : Browsable<CollectionMusicBrainzModel>
