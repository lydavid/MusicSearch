package ly.david.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.GenreMusicBrainzModel
import ly.david.data.network.InstrumentMusicBrainzModel
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.SeriesMusicBrainzModel
import ly.david.data.network.WorkMusicBrainzModel

private const val MUSIC_BRAINZ_API_BASE_URL = "$MUSIC_BRAINZ_BASE_URL/ws/2/"
private const val USER_AGENT_VALUE = "MusicSearch (https://github.com/lydavid/MusicSearch)"

interface MusicBrainzApi : SearchApi, BrowseApi, LookupApi, CollectionApi {
    companion object {
        fun create(): MusicBrainzApi {
            val client = HttpClient(Android) {
                defaultRequest {
                    userAgent(USER_AGENT_VALUE)
                    url(MUSIC_BRAINZ_API_BASE_URL)
                }
                install(Logging) {
                    level = LogLevel.ALL
                }
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            isLenient = true
                        }
                    )
                }
//                install(Auth) {
//                    bearer {
//                        loadTokens {
//                            val accessToken = spotifyOAuth.getAccessToken() ?: return@loadTokens null
//                            BearerTokens(accessToken, "")
//                        }
//                        refreshTokens {
//                            val newAccessToken = spotifyAuthApi.getAccessToken(
//                                clientId = clientId,
//                                clientSecret = clientSecret,
//                            )
//                            spotifyOAuth.saveAccessToken(
//                                accessToken = newAccessToken.accessToken,
//                            )
//
//                            val accessToken = spotifyOAuth.getAccessToken() ?: return@refreshTokens null
//                            BearerTokens(accessToken, "")
//                        }
//                        sendWithoutRequest { request ->
//                            request.url.host == HOST
//                        }
//                    }
//                }
            }

            return MusicBrainzApiImpl(
                client = client,
            )
        }
    }
}

class MusicBrainzApiImpl(
    override val client: HttpClient,
) : SearchApiImpl, MusicBrainzApi {

    override suspend fun browseAreasByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseAreasResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseArtistsByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseArtistsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseCollectionsByUser(
        username: String,
        limit: Int,
        offset: Int,
        include: String?,
    ): BrowseCollectionsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseEventsByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseEventsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseEventsByPlace(placeId: String, limit: Int, offset: Int): BrowseEventsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseInstrumentsByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseInstrumentsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseLabelsByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseLabelsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browsePlacesByArea(areaId: String, limit: Int, offset: Int): BrowsePlacesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browsePlacesByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowsePlacesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseRecordingsByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseRecordingsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseRecordingsByWork(workId: String, limit: Int, offset: Int): BrowseRecordingsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseReleasesByArea(areaId: String, limit: Int, offset: Int): BrowseReleasesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseReleasesByArtist(artistId: String, limit: Int, offset: Int): BrowseReleasesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseReleasesByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseReleasesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseReleasesByLabel(
        labelId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleasesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseReleasesByRecording(
        recordingId: String,
        limit: Int,
        offset: Int,
    ): BrowseReleasesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseReleasesByReleaseGroup(
        releaseGroupId: String,
        limit: Int,
        offset: Int,
    ): BrowseReleasesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseReleaseGroupsByArtist(
        artistId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleaseGroupsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseReleaseGroupsByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleaseGroupsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseSeriesByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseSeriesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseWorksByCollection(
        bearerToken: String?,
        collectionId: String,
        limit: Int,
        offset: Int,
    ): BrowseWorksResponse {
        TODO("Not yet implemented")
    }

    override suspend fun lookupArea(areaId: String, include: String?): AreaMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupArtist(artistId: String, include: String?): ArtistMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupEvent(eventId: String, include: String?): EventMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupGenre(genreId: String, include: String?): GenreMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupInstrument(instrumentId: String, include: String): InstrumentMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupLabel(labelId: String, include: String): LabelMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupPlace(placeId: String, include: String?): PlaceMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupRecording(recordingId: String, include: String): RecordingMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupRelease(releaseId: String, include: String): ReleaseMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupReleaseGroup(releaseGroupId: String, include: String): ReleaseGroupMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupSeries(seriesId: String, include: String?): SeriesMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun lookupWork(workId: String, include: String?): WorkMusicBrainzModel {
        TODO("Not yet implemented")
    }

    override suspend fun uploadToCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: String,
        client: String,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFromCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: String,
        client: String,
    ) {
        TODO("Not yet implemented")
    }
}
