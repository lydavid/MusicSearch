package ly.david.musicbrainzjetpackcompose.data.browse

import com.squareup.moshi.Json
import ly.david.musicbrainzjetpackcompose.data.ReleaseGroup
import retrofit2.http.GET
import retrofit2.http.Query

const val DEFAULT_BROWSE_LIMIT = 100

/**
 * See [browse API](https://wiki.musicbrainz.org/MusicBrainz_API#Browse).
 *
 * Get entities directly linked to another entity. Such as all release groups by an artist.
 * This is the only type of request with pagination.
 */
interface Browse {

    @GET("release-group")
    suspend fun browseReleaseGroupsByArtist(
        @Query("artist") artistId: String,
        @Query("limit") limit: Int = DEFAULT_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0
    ): BrowseReleaseGroupsResponse
}

data class BrowseReleaseGroupsResponse(
    @Json(name = "release-group-count") val releaseGroupCount: Int,
    @Json(name = "release-group-offset") val releaseGroupOffset: Int,
    @Json(name = "release-groups") val releaseGroups: List<ReleaseGroup>
)
