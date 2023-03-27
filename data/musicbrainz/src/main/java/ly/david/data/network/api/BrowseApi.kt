package ly.david.data.network.api

import com.squareup.moshi.Json
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.InstrumentMusicBrainzModel
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.network.MusicBrainzModel
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.SeriesMusicBrainzModel
import ly.david.data.network.WorkMusicBrainzModel
import retrofit2.http.GET
import retrofit2.http.Query

internal const val RELEASE_GROUPS = "release-groups"
internal const val LABELS = "labels"

/**
 * See [browse API](https://wiki.musicbrainz.org/MusicBrainz_API#Browse).
 *
 * Get entities directly linked to another entity. Such as all release groups by an artist.
 * This is the only type of request with pagination.
 */
interface BrowseApi {

    @GET("area")
    suspend fun browseAreasByCollection(
        @Query("collection") collectionId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): BrowseAreasResponse

    @GET("artist")
    suspend fun browseArtistsByCollection(
        @Query("collection") collectionId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): BrowseArtistsResponse

    @GET("event")
    suspend fun browseEventsByCollection(
        @Query("collection") collectionId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): BrowseEventsResponse

    @GET("event")
    suspend fun browseEventsByPlace(
        @Query("place") placeId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): BrowseEventsResponse

    @GET("instrument")
    suspend fun browseInstrumentsByCollection(
        @Query("collection") collectionId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): BrowseInstrumentsResponse

    @GET("label")
    suspend fun browseLabelsByCollection(
        @Query("collection") collectionId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): BrowseLabelsResponse

    @GET("place")
    suspend fun browsePlacesByArea(
        @Query("area") areaId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): BrowsePlacesResponse

    @GET("place")
    suspend fun browsePlacesByCollection(
        @Query("collection") collectionId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): BrowsePlacesResponse

    @GET("recording")
    suspend fun browseRecordingsByCollection(
        @Query("collection") collectionId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): BrowseRecordingsResponse

    @GET("recording")
    suspend fun browseRecordingsByWork(
        @Query("work") workId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
    ): BrowseRecordingsResponse

    @GET("release")
    suspend fun browseReleasesByArea(
        @Query("area") areaId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0
    ): BrowseReleasesResponse

    @GET("release")
    suspend fun browseReleasesByArtist(
        @Query("artist") artistId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0
    ): BrowseReleasesResponse

    @GET("release")
    suspend fun browseReleasesByCollection(
        @Query("collection") collectionId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0
    ): BrowseReleasesResponse

    @GET("release")
    suspend fun browseReleasesByLabel(
        @Query("label") labelId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
        @Query("inc") include: String = LABELS
    ): BrowseReleasesResponse

    @GET("release")
    suspend fun browseReleasesByRecording(
        @Query("recording") recordingId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0
    ): BrowseReleasesResponse

    @GET("release")
    suspend fun browseReleasesByReleaseGroup(
        @Query("release-group") releaseGroupId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0
//        @Query("inc") include: String = RELEASE_GROUPS
        // artist-credits, labels, recordings, release-groups, media, discids, isrcs (with recordings)
        // todo if our condition for looking up release is that formats and tracks are populated, then we can't inc media here
    ): BrowseReleasesResponse

    @GET("release-group")
    suspend fun browseReleaseGroupsByArtist(
        @Query("artist") artistId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
        @Query("inc") include: String = "artist-credits"
    ): BrowseReleaseGroupsResponse

    @GET("release-group")
    suspend fun browseReleaseGroupsByCollection(
        @Query("collection") collectionId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0,
        @Query("inc") include: String = "artist-credits"
    ): BrowseReleaseGroupsResponse

    @GET("series")
    suspend fun browseSeriesByCollection(
        @Query("collection") collectionId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0
    ): BrowseSeriesResponse

    @GET("work")
    suspend fun browseWorksByCollection(
        @Query("collection") collectionId: String,
        @Query("limit") limit: Int = SEARCH_BROWSE_LIMIT,
        @Query("offset") offset: Int = 0
    ): BrowseWorksResponse
}

/**
 * Generic response fields from a Browse request.
 */
interface Browsable<MM : MusicBrainzModel> {
    val count: Int
    val offset: Int
    val musicBrainzModels: List<MM>
}

data class BrowseAreasResponse(
    @Json(name = "area-count") override val count: Int,
    @Json(name = "area-offset") override val offset: Int,
    @Json(name = "areas") override val musicBrainzModels: List<AreaMusicBrainzModel>
) : Browsable<AreaMusicBrainzModel>

data class BrowseArtistsResponse(
    @Json(name = "artist-count") override val count: Int,
    @Json(name = "artist-offset") override val offset: Int,
    @Json(name = "artists") override val musicBrainzModels: List<ArtistMusicBrainzModel>
) : Browsable<ArtistMusicBrainzModel>

data class BrowseEventsResponse(
    @Json(name = "event-count") override val count: Int,
    @Json(name = "event-offset") override val offset: Int,
    @Json(name = "events") override val musicBrainzModels: List<EventMusicBrainzModel>
) : Browsable<EventMusicBrainzModel>

data class BrowseInstrumentsResponse(
    @Json(name = "instrument-count") override val count: Int,
    @Json(name = "instrument-offset") override val offset: Int,
    @Json(name = "instruments") override val musicBrainzModels: List<InstrumentMusicBrainzModel>
) : Browsable<InstrumentMusicBrainzModel>

data class BrowseLabelsResponse(
    @Json(name = "label-count") override val count: Int,
    @Json(name = "label-offset") override val offset: Int,
    @Json(name = "labels") override val musicBrainzModels: List<LabelMusicBrainzModel>
) : Browsable<LabelMusicBrainzModel>


data class BrowsePlacesResponse(
    @Json(name = "place-count") override val count: Int,
    @Json(name = "place-offset") override val offset: Int,
    @Json(name = "places") override val musicBrainzModels: List<PlaceMusicBrainzModel>
) : Browsable<PlaceMusicBrainzModel>

data class BrowseRecordingsResponse(
    @Json(name = "recording-count") override val count: Int,
    @Json(name = "recording-offset") override val offset: Int,
    @Json(name = "recordings") override val musicBrainzModels: List<RecordingMusicBrainzModel>
) : Browsable<RecordingMusicBrainzModel>

data class BrowseReleasesResponse(
    @Json(name = "release-count") override val count: Int,
    @Json(name = "release-offset") override val offset: Int,
    @Json(name = "releases") override val musicBrainzModels: List<ReleaseMusicBrainzModel>
) : Browsable<ReleaseMusicBrainzModel>

data class BrowseReleaseGroupsResponse(
    @Json(name = "release-group-count") override val count: Int,
    @Json(name = "release-group-offset") override val offset: Int,
    @Json(name = "release-groups") override val musicBrainzModels: List<ReleaseGroupMusicBrainzModel>
) : Browsable<ReleaseGroupMusicBrainzModel>

data class BrowseSeriesResponse(
    @Json(name = "series-count") override val count: Int,
    @Json(name = "series-offset") override val offset: Int,
    @Json(name = "series") override val musicBrainzModels: List<SeriesMusicBrainzModel>
) : Browsable<SeriesMusicBrainzModel>

data class BrowseWorksResponse(
    @Json(name = "work-count") override val count: Int,
    @Json(name = "work-offset") override val offset: Int,
    @Json(name = "works") override val musicBrainzModels: List<WorkMusicBrainzModel>
) : Browsable<WorkMusicBrainzModel>
