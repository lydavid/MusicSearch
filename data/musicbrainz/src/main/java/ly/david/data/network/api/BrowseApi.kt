package ly.david.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.data.AUTHORIZATION
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.network.CollectionMusicBrainzModel
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

internal const val LABELS = "labels"

/**
 * See [browse API](https://wiki.musicbrainz.org/MusicBrainz_API#Browse).
 *
 * Get entities directly linked to another entity. Such as all release groups by an artist.
 * This is the only type of request with pagination.
 */
interface BrowseApi {

    suspend fun browseAreasByCollection(
        bearerToken: String? = null,
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseAreasResponse

    suspend fun browseArtistsByCollection(
        bearerToken: String? = null,
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseArtistsResponse

    suspend fun browseCollectionsByUser(
        username: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
        include: String? = null,
    ): BrowseCollectionsResponse

    suspend fun browseEventsByCollection(
        bearerToken: String? = null,
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseEventsResponse

    suspend fun browseEventsByPlace(
        placeId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseEventsResponse

    suspend fun browseInstrumentsByCollection(
        bearerToken: String? = null,
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseInstrumentsResponse

    suspend fun browseLabelsByCollection(
        bearerToken: String? = null,
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseLabelsResponse

    suspend fun browsePlacesByArea(
        areaId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowsePlacesResponse

    suspend fun browsePlacesByCollection(
        bearerToken: String? = null,
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowsePlacesResponse

    suspend fun browseRecordingsByCollection(
        bearerToken: String? = null,
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseRecordingsResponse

    suspend fun browseRecordingsByWork(
        workId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseRecordingsResponse

    suspend fun browseReleasesByArea(
        areaId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseReleasesResponse

    suspend fun browseReleasesByArtist(
        artistId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseReleasesResponse

    suspend fun browseReleasesByCollection(
        bearerToken: String? = null,
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseReleasesResponse

    suspend fun browseReleasesByLabel(
        labelId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
        include: String = LABELS,
    ): BrowseReleasesResponse

    suspend fun browseReleasesByRecording(
        recordingId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseReleasesResponse

    suspend fun browseReleasesByReleaseGroup(
        releaseGroupId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseReleasesResponse

    suspend fun browseReleaseGroupsByArtist(
        artistId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
        include: String = "artist-credits",
    ): BrowseReleaseGroupsResponse

    suspend fun browseReleaseGroupsByCollection(
        bearerToken: String? = null,
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
        include: String = "artist-credits",
    ): BrowseReleaseGroupsResponse

    suspend fun browseSeriesByCollection(
        bearerToken: String? = null,
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseSeriesResponse

    suspend fun browseWorksByCollection(
        bearerToken: String? = null,
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseWorksResponse
}

interface BrowseApiImpl : BrowseApi {
    val httpClient: HttpClient

    override suspend fun browseAreasByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseAreasResponse {
        return httpClient.get {
            url {
                appendPathSegments("area")
                header(AUTHORIZATION, bearerToken)
                parameter("collection", collectionId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseArtistsByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseArtistsResponse {
        return httpClient.get {
            url {
                appendPathSegments("artist")
                header(AUTHORIZATION, bearerToken)
                parameter("collection", collectionId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
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
                parameter("editor", username)
                parameter("limit", limit)
                parameter("offset", offset)
                parameter("inc", include)
            }
        }.body()
    }

    override suspend fun browseEventsByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseEventsResponse {
        return httpClient.get {
            url {
                appendPathSegments("event")
                header(AUTHORIZATION, bearerToken)
                parameter("collection", collectionId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseEventsByPlace(
        placeId: String,
        limit: Int,
        offset: Int,
    ): BrowseEventsResponse {
        return httpClient.get {
            url {
                appendPathSegments("event")
                parameter("place", placeId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseInstrumentsByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseInstrumentsResponse {
        return httpClient.get {
            url {
                appendPathSegments("instrument")
                header(AUTHORIZATION, bearerToken)
                parameter("collection", collectionId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseLabelsByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseLabelsResponse {
        return httpClient.get {
            url {
                appendPathSegments("label")
                header(AUTHORIZATION, bearerToken)
                parameter("collection", collectionId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browsePlacesByArea(
        areaId: String,
        limit: Int,
        offset: Int,
    ): BrowsePlacesResponse {
        return httpClient.get {
            url {
                appendPathSegments("place")
                parameter("area", areaId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browsePlacesByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowsePlacesResponse {
        return httpClient.get {
            url {
                appendPathSegments("place")
                header(AUTHORIZATION, bearerToken)
                parameter("collection", collectionId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseRecordingsByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseRecordingsResponse {
        return httpClient.get {
            url {
                appendPathSegments("recording")
                header(AUTHORIZATION, bearerToken)
                parameter("collection", collectionId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseRecordingsByWork(
        workId: String,
        limit: Int,
        offset: Int,
    ): BrowseRecordingsResponse {
        return httpClient.get {
            url {
                appendPathSegments("recording")
                parameter("work", workId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseReleasesByArea(
        areaId: String,
        limit: Int,
        offset: Int,
    ): BrowseReleasesResponse {
        return httpClient.get {
            url {
                appendPathSegments("release")
                parameter("area", areaId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseReleasesByArtist(
        artistId: String,
        limit: Int,
        offset: Int,
    ): BrowseReleasesResponse {
        return httpClient.get {
            url {
                appendPathSegments("release")
                parameter("artist", artistId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseReleasesByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseReleasesResponse {
        return httpClient.get {
            url {
                appendPathSegments("release")
                header(AUTHORIZATION, bearerToken)
                parameter("collection", collectionId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseReleasesByLabel(
        labelId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleasesResponse {
        return httpClient.get {
            url {
                appendPathSegments("release")
                parameter("label", labelId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseReleasesByRecording(
        recordingId: String,
        limit: Int,
        offset: Int,
    ): BrowseReleasesResponse {
        return httpClient.get {
            url {
                appendPathSegments("release")
                parameter("recording", recordingId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseReleasesByReleaseGroup(
        releaseGroupId: String,
        limit: Int,
        offset: Int,
    ): BrowseReleasesResponse {
        return httpClient.get {
            url {
                appendPathSegments("release")
                parameter("release-group", releaseGroupId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseReleaseGroupsByArtist(
        artistId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleaseGroupsResponse {
        return httpClient.get {
            url {
                appendPathSegments("release-group")
                parameter("artist", artistId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseReleaseGroupsByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleaseGroupsResponse {
        return httpClient.get {
            url {
                appendPathSegments("release-group")
                header(AUTHORIZATION, bearerToken)
                parameter("collection", collectionId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseSeriesByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseSeriesResponse {
        return httpClient.get {
            url {
                appendPathSegments("series")
                header(AUTHORIZATION, bearerToken)
                parameter("collection", collectionId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }

    override suspend fun browseWorksByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseWorksResponse {
        return httpClient.get {
            url {
                appendPathSegments("work")
                header(AUTHORIZATION, bearerToken)
                parameter("collection", collectionId)
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }.body()
    }
}

/**
 * Generic response fields from a Browse request.
 */
interface Browsable<MM : MusicBrainzModel> {
    val count: Int
    val offset: Int
    val musicBrainzModels: List<MM>
}

@Serializable
data class BrowseAreasResponse(
    @SerialName("area-count") override val count: Int,
    @SerialName("area-offset") override val offset: Int,
    @SerialName("areas") override val musicBrainzModels: List<AreaMusicBrainzModel>,
) : Browsable<AreaMusicBrainzModel>

@Serializable
data class BrowseArtistsResponse(
    @SerialName("artist-count") override val count: Int,
    @SerialName("artist-offset") override val offset: Int,
    @SerialName("artists") override val musicBrainzModels: List<ArtistMusicBrainzModel>,
) : Browsable<ArtistMusicBrainzModel>

@Serializable
data class BrowseCollectionsResponse(
    @SerialName("collection-count") override val count: Int,
    @SerialName("collection-offset") override val offset: Int,
    @SerialName("collections") override val musicBrainzModels: List<CollectionMusicBrainzModel>,
) : Browsable<CollectionMusicBrainzModel>

@Serializable
data class BrowseEventsResponse(
    @SerialName("event-count") override val count: Int,
    @SerialName("event-offset") override val offset: Int,
    @SerialName("events") override val musicBrainzModels: List<EventMusicBrainzModel>,
) : Browsable<EventMusicBrainzModel>

@Serializable
data class BrowseInstrumentsResponse(
    @SerialName("instrument-count") override val count: Int,
    @SerialName("instrument-offset") override val offset: Int,
    @SerialName("instruments") override val musicBrainzModels: List<InstrumentMusicBrainzModel>,
) : Browsable<InstrumentMusicBrainzModel>

@Serializable
data class BrowseLabelsResponse(
    @SerialName("label-count") override val count: Int,
    @SerialName("label-offset") override val offset: Int,
    @SerialName("labels") override val musicBrainzModels: List<LabelMusicBrainzModel>,
) : Browsable<LabelMusicBrainzModel>

@Serializable
data class BrowsePlacesResponse(
    @SerialName("place-count") override val count: Int,
    @SerialName("place-offset") override val offset: Int,
    @SerialName("places") override val musicBrainzModels: List<PlaceMusicBrainzModel>,
) : Browsable<PlaceMusicBrainzModel>

@Serializable
data class BrowseRecordingsResponse(
    @SerialName("recording-count") override val count: Int,
    @SerialName("recording-offset") override val offset: Int,
    @SerialName("recordings") override val musicBrainzModels: List<RecordingMusicBrainzModel>,
) : Browsable<RecordingMusicBrainzModel>

@Serializable
data class BrowseReleasesResponse(
    @SerialName("release-count") override val count: Int,
    @SerialName("release-offset") override val offset: Int,
    @SerialName("releases") override val musicBrainzModels: List<ReleaseMusicBrainzModel>,
) : Browsable<ReleaseMusicBrainzModel>

@Serializable
data class BrowseReleaseGroupsResponse(
    @SerialName("release-group-count") override val count: Int,
    @SerialName("release-group-offset") override val offset: Int,
    @SerialName("release-groups") override val musicBrainzModels: List<ReleaseGroupMusicBrainzModel>,
) : Browsable<ReleaseGroupMusicBrainzModel>

@Serializable
data class BrowseSeriesResponse(
    @SerialName("series-count") override val count: Int,
    @SerialName("series-offset") override val offset: Int,
    @SerialName("series") override val musicBrainzModels: List<SeriesMusicBrainzModel>,
) : Browsable<SeriesMusicBrainzModel>

@Serializable
data class BrowseWorksResponse(
    @SerialName("work-count") override val count: Int,
    @SerialName("work-offset") override val offset: Int,
    @SerialName("works") override val musicBrainzModels: List<WorkMusicBrainzModel>,
) : Browsable<WorkMusicBrainzModel>
