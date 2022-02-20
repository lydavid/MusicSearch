package ly.david.musicbrainzjetpackcompose.data.browse

import com.squareup.moshi.Json
import ly.david.musicbrainzjetpackcompose.data.Recording
import ly.david.musicbrainzjetpackcompose.data.Release
import ly.david.musicbrainzjetpackcompose.data.ReleaseGroup
import ly.david.musicbrainzjetpackcompose.preferences.MAX_BROWSE_LIMIT
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * See [browse API](https://wiki.musicbrainz.org/MusicBrainz_API#Browse).
 *
 * Get entities directly linked to another entity. Such as all release groups by an artist.
 * This is the only type of request with pagination.
 */
interface Browse {

    // TODO: inc=artist-credits so that we can update title of screen with artist name
    @GET("release-group")
    suspend fun browseReleaseGroupsByArtist(
        @Query("artist") artistId: String,
        @Query("limit") limit: Int = MAX_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0
    ): BrowseReleaseGroupsResponse

    @GET("release")
    suspend fun browseReleasesByReleaseGroup(
        @Query("release-group") releaseGroupId: String,
        @Query("limit") limit: Int = MAX_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
        @Query("inc") include: String = "artist-credits+labels+media"
    ): BrowseReleasesResponse

    @GET("recording")
    suspend fun browseRecordingsByRelease(
        @Query("release") releaseId: String,
        @Query("limit") limit: Int = MAX_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0
    ): BrowseRecordingsResponse
}

data class BrowseReleaseGroupsResponse(
    @Json(name = "release-group-count") val releaseGroupCount: Int,
    @Json(name = "release-group-offset") val releaseGroupOffset: Int,
    @Json(name = "release-groups") val releaseGroups: List<ReleaseGroup>
)

data class BrowseReleasesResponse(
    @Json(name = "release-count") val releaseCount: Int,
    @Json(name = "release-offset") val releaseOffset: Int,
    @Json(name = "releases") val releases: List<Release>
)

data class BrowseRecordingsResponse(
    @Json(name = "recordings-count") val recordingCount: Int,
    @Json(name = "recordings-offset") val recordingOffset: Int,
    @Json(name = "recordings") val recordings: List<Recording>
)
