package ly.david.musicbrainzjetpackcompose.data.search

import com.squareup.moshi.Json
import ly.david.musicbrainzjetpackcompose.data.Artist
import retrofit2.http.GET
import retrofit2.http.Query

private const val DEFAULT_SEARCH_LIMIT = 25

/**
 * Search for MusicBrainz entities using text.
 */
interface Search {

    @GET("artist")
    suspend fun queryArtists(
        @Query("query") query: String,
        @Query("limit") limit: Int = DEFAULT_SEARCH_LIMIT
    ): SearchArtistsResponse
}

data class SearchArtistsResponse(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "artists") val artists: List<Artist> // Max of 25 at a time
)
