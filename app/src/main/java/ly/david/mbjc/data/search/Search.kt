package ly.david.mbjc.data.search

import com.squareup.moshi.Json
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.network.SEARCH_LIMIT
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Search for MusicBrainz entities using text.
 */
interface Search {

    @GET("artist")
    suspend fun queryArtists(
        @Query("query") query: String,
        @Query("limit") limit: Int = SEARCH_LIMIT,
        @Query("offset") offset: Int = 0,
    ): SearchArtistsResponse
}

data class SearchArtistsResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "artists") val artists: List<Artist> // Max of 25 at a time
)
