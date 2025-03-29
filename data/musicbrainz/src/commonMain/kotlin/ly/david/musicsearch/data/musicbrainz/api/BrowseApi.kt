package ly.david.musicsearch.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.resourceUri
import ly.david.musicsearch.data.musicbrainz.SEARCH_BROWSE_LIMIT
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.CollectionMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.MusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzModel

const val ARTIST_CREDITS = "artist-credits"
const val LABELS = "labels"

/**
 * See [browse API](https://wiki.musicbrainz.org/MusicBrainz_API#Browse).
 *
 * Get entities directly linked to another entity. Such as all release groups by an artist.
 * This is the only type of request with pagination.
 */
interface BrowseApi {

    suspend fun browseAreasByCollection(
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseAreasResponse

    suspend fun browseArtistsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseArtistsResponse

    suspend fun browseEventsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseEventsResponse

    suspend fun browseGenresByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseGenresResponse

    suspend fun browseInstrumentsByCollection(
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseInstrumentsResponse

    suspend fun browseLabelsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseLabelsResponse

    suspend fun browsePlacesByArea(
        areaId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowsePlacesResponse

    suspend fun browsePlacesByCollection(
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowsePlacesResponse

    suspend fun browseRecordingsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
        include: String = ARTIST_CREDITS,
    ): BrowseRecordingsResponse

    suspend fun browseReleasesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
        include: String = ARTIST_CREDITS,
    ): BrowseReleasesResponse

    suspend fun browseReleaseGroupsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
        include: String = ARTIST_CREDITS,
    ): BrowseReleaseGroupsResponse

    suspend fun browseSeriesByCollection(
        collectionId: String,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseSeriesResponse

    suspend fun browseWorksByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int = SEARCH_BROWSE_LIMIT,
        offset: Int = 0,
    ): BrowseWorksResponse
}

interface BrowseApiImpl : BrowseApi {
    val httpClient: HttpClient

    override suspend fun browseAreasByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseAreasResponse {
        return httpClient.get {
            url {
                appendPathSegments("area")
                parameter(
                    "collection",
                    collectionId,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
            }
        }.body()
    }

    override suspend fun browseArtistsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
    ): BrowseArtistsResponse {
        return httpClient.get {
            url {
                appendPathSegments("artist")
                parameter(
                    entity.resourceUri,
                    entityId,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
            }
        }.body()
    }

    override suspend fun browseEventsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
    ): BrowseEventsResponse {
        return httpClient.get {
            url {
                appendPathSegments("event")
                parameter(
                    entity.resourceUri,
                    entityId,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
            }
        }.body()
    }

    override suspend fun browseGenresByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
    ): BrowseGenresResponse {
        return httpClient.get {
            url {
                appendPathSegments("genre")
                parameter(
                    entity.resourceUri,
                    entityId,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
            }
        }.body()
    }

    override suspend fun browseInstrumentsByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseInstrumentsResponse {
        return httpClient.get {
            url {
                appendPathSegments("instrument")
                parameter(
                    "collection",
                    collectionId,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
            }
        }.body()
    }

    override suspend fun browseLabelsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
    ): BrowseLabelsResponse {
        return httpClient.get {
            url {
                appendPathSegments("label")
                parameter(
                    entity.resourceUri,
                    entityId,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
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
                parameter(
                    "area",
                    areaId,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
            }
        }.body()
    }

    override suspend fun browsePlacesByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowsePlacesResponse {
        return httpClient.get {
            url {
                appendPathSegments("place")
                parameter(
                    "collection",
                    collectionId,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
            }
        }.body()
    }

    override suspend fun browseRecordingsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseRecordingsResponse {
        return httpClient.get {
            url {
                appendPathSegments("recording")
                parameter(
                    entity.resourceUri,
                    entityId,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
                parameter(
                    "inc",
                    include,
                )
            }
        }.body()
    }

    override suspend fun browseReleasesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleasesResponse {
        return httpClient.get {
            url {
                appendPathSegments("release")
                parameter(
                    entity.resourceUri,
                    entityId,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
                parameter(
                    "inc",
                    include,
                )
            }
        }.body()
    }

    override suspend fun browseReleaseGroupsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleaseGroupsResponse {
        return httpClient.get {
            url {
                appendPathSegments("release-group")
                parameter(
                    entity.resourceUri,
                    entityId,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
                parameter(
                    "inc",
                    include,
                )
            }
        }.body()
    }

    override suspend fun browseSeriesByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseSeriesResponse {
        return httpClient.get {
            url {
                appendPathSegments("series")
                parameter(
                    "collection",
                    collectionId,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
            }
        }.body()
    }

    override suspend fun browseWorksByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
    ): BrowseWorksResponse {
        return httpClient.get {
            url {
                appendPathSegments("work")
                parameter(
                    entity.resourceUri,
                    entityId,
                )
                parameter(
                    "limit",
                    limit,
                )
                parameter(
                    "offset",
                    offset,
                )
            }
        }.body()
    }
}

/**
 * Generic response fields from a Browse request.
 */
interface Browsable<MB : MusicBrainzModel> {
    val count: Int
    val offset: Int
    val musicBrainzModels: List<MB>
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
    @SerialName("collection-count") override val count: Int = 0,
    @SerialName("collection-offset") override val offset: Int = 0,
    @SerialName("collections") override val musicBrainzModels: List<CollectionMusicBrainzModel> = listOf(),
) : Browsable<CollectionMusicBrainzModel>

@Serializable
data class BrowseEventsResponse(
    @SerialName("event-count") override val count: Int,
    @SerialName("event-offset") override val offset: Int,
    @SerialName("events") override val musicBrainzModels: List<EventMusicBrainzModel>,
) : Browsable<EventMusicBrainzModel>

@Serializable
data class BrowseGenresResponse(
    @SerialName("genre-count") override val count: Int,
    @SerialName("genre-offset") override val offset: Int,
    @SerialName("genres") override val musicBrainzModels: List<GenreMusicBrainzModel>,
) : Browsable<GenreMusicBrainzModel>

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
