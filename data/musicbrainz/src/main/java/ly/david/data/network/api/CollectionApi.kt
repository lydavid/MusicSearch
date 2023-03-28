package ly.david.data.network.api

import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CollectionApi {

    companion object {
        const val USER_COLLECTIONS = "user-collections"
    }

    @PUT("collection/{collectionId}/{resourceUriPlural}/{mbids}")
    suspend fun uploadToCollection(
        @Path("collectionId") collectionId: String,
        @Path("resourceUriPlural") resourceUriPlural: String,
        @Path("mbids") mbids: String,
        @Query("client") client: String = "MusicSearch"
    )
}
