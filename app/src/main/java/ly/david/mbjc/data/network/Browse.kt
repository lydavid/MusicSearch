package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.Recording
import retrofit2.http.GET
import retrofit2.http.Query

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
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
        @Query("inc") include: String = "artist-credits"
    ): BrowseReleaseGroupsResponse

    // can browse by area, artist, collection, label, track, track_artist, recording, release-group
    // currently only browsing by release-group
    @GET("release")
    suspend fun browseReleasesByReleaseGroup(
        @Query("release-group") releaseGroupId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
        // artist-credits, labels, recordings, release-groups, media, discids, isrcs (with recordings)
        // todo if our condition for looking up release is that formats and tracks are populated, then we can't inc media here
//        @Query("inc") include: String = "media"
    ): BrowseReleasesResponse

    @GET("recording")
    suspend fun browseRecordingsByRelease(
        @Query("release") releaseId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0
    ): BrowseRecordingsResponse
}

/**
 * Generic response fields when from a Browse request.
 */
interface Browsable {
    val count: Int
    val offset: Int
}

data class BrowseReleaseGroupsResponse(
    @Json(name = "release-group-count") override val count: Int,
    @Json(name = "release-group-offset") override val offset: Int,
    @Json(name = "release-groups") val releaseGroups: List<MusicBrainzReleaseGroup>
): Browsable

data class BrowseReleasesResponse(
    @Json(name = "release-count") override val count: Int,
    @Json(name = "release-offset") override val offset: Int,
    @Json(name = "releases") val musicBrainzReleases: List<MusicBrainzRelease>
): Browsable

data class BrowseRecordingsResponse(
    @Json(name = "recordings-count") val recordingCount: Int,
    @Json(name = "recordings-offset") val recordingOffset: Int,
    @Json(name = "recordings") val recordings: List<Recording>
)
