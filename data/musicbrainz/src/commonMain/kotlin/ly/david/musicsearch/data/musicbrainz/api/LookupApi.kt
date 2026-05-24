package ly.david.musicsearch.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.UrlMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.relatableEntities
import ly.david.musicsearch.shared.domain.network.resourceUri

const val INCLUDE = "inc"
const val RECORDING_REL = "recording-rels"
private const val URL_REL = "url-rels"
private const val TAGS = "tags"
const val USER_TAGS = "user-tags"
private const val GENRES = "genres"
private const val USER_GENRES = "user-genres"
const val GENERAL_LOOKUP_INCLUDES = "$URL_REL+$ALIASES+$TAGS+$GENRES"
const val USER_LOOKUP_INCLUDES = "$USER_TAGS+$USER_GENRES"

/**
 * See [lookup API](https://wiki.musicbrainz.org/MusicBrainz_API#Lookups).
 *
 * Gets information for a specific entity.
 * Can include additional information related to the entity. Max of 25.
 */
interface LookupApi {

    suspend fun lookupArea(
        areaId: String,
        include: String?,

        // TODO: Separate tab: artists, events, labels, releases, recordings, places, works
        //  we might be able to do paged browse requests for these
        //  place by area id

        // TODO: place-rels doesn't return anything
        //  it isn't enough to get the data on this page: https://musicbrainz.org/area/74e50e58-5deb-4b99-93a2-decbb365c07f/places
    ): AreaMusicBrainzNetworkModel

    suspend fun lookupArtist(
        artistId: String,
        include: String?,
    ): ArtistMusicBrainzNetworkModel

    suspend fun lookupEvent(
        eventId: String,
        include: String?,
    ): EventMusicBrainzNetworkModel

    suspend fun lookupGenre(
        genreId: String,
        include: String?,
    ): GenreMusicBrainzNetworkModel

    suspend fun lookupInstrument(
        instrumentId: String,
        include: String,
    ): InstrumentMusicBrainzNetworkModel

    suspend fun lookupLabel(
        labelId: String,
        include: String,
    ): LabelMusicBrainzNetworkModel

    suspend fun lookupPlace(
        placeId: String,
        include: String?,
    ): PlaceMusicBrainzNetworkModel

    suspend fun lookupRecording(
        recordingId: String,
        include: String,
    ): RecordingMusicBrainzNetworkModel

    suspend fun lookupRelease(
        releaseId: String,
        include: String,
    ): ReleaseMusicBrainzNetworkModel

    suspend fun lookupReleaseGroup(
        releaseGroupId: String,
        include: String,
    ): ReleaseGroupMusicBrainzNetworkModel

    suspend fun lookupSeries(
        seriesId: String,
        include: String?,
    ): SeriesMusicBrainzNetworkModel

    suspend fun lookupWork(
        workId: String,
        include: String?,
    ): WorkMusicBrainzNetworkModel

    suspend fun lookupUrl(
        url: String,
    ): UrlMusicBrainzNetworkModel
}

interface LookupApiImpl : LookupApi {
    val httpClient: HttpClient

    override suspend fun lookupArea(
        areaId: String,
        include: String?,
    ): AreaMusicBrainzNetworkModel {
        return httpClient.get {
            url {
                appendPathSegments("area", areaId)
                parameter(INCLUDE, include)
            }
        }.body()
    }

    override suspend fun lookupArtist(
        artistId: String,
        include: String?,
    ): ArtistMusicBrainzNetworkModel {
        return httpClient.get {
            url {
                appendPathSegments("artist", artistId)
                parameter(INCLUDE, include)
                parameter("fmt", "json")
            }
        }.body()
    }

    override suspend fun lookupEvent(
        eventId: String,
        include: String?,
    ): EventMusicBrainzNetworkModel {
        return httpClient.get {
            url {
                appendPathSegments("event", eventId)
                parameter(INCLUDE, include)
            }
        }.body()
    }

    override suspend fun lookupGenre(
        genreId: String,
        include: String?,
    ): GenreMusicBrainzNetworkModel {
        return httpClient.get {
            url {
                appendPathSegments("genre", genreId)
                parameter(INCLUDE, include)
            }
        }.body()
    }

    override suspend fun lookupInstrument(
        instrumentId: String,
        include: String,
    ): InstrumentMusicBrainzNetworkModel {
        return httpClient.get {
            url {
                appendPathSegments("instrument", instrumentId)
                parameter(INCLUDE, include)
            }
        }.body()
    }

    override suspend fun lookupLabel(
        labelId: String,
        include: String,
    ): LabelMusicBrainzNetworkModel {
        return httpClient.get {
            url {
                appendPathSegments("label", labelId)
                parameter(INCLUDE, include)
            }
        }.body()
    }

    override suspend fun lookupPlace(
        placeId: String,
        include: String?,
    ): PlaceMusicBrainzNetworkModel {
        return httpClient.get {
            url {
                appendPathSegments("place", placeId)
                parameter(INCLUDE, include)
            }
        }.body()
    }

    override suspend fun lookupRecording(
        recordingId: String,
        include: String,
    ): RecordingMusicBrainzNetworkModel {
        return httpClient.get {
            url {
                appendPathSegments("recording", recordingId)
                parameter(INCLUDE, include)
            }
        }.body()
    }

    override suspend fun lookupRelease(
        releaseId: String,
        include: String,
    ): ReleaseMusicBrainzNetworkModel {
        return httpClient.get {
            url {
                appendPathSegments("release", releaseId)
                parameter(INCLUDE, include)
            }
        }.body()
    }

    override suspend fun lookupReleaseGroup(
        releaseGroupId: String,
        include: String,
    ): ReleaseGroupMusicBrainzNetworkModel {
        return httpClient.get {
            url {
                appendPathSegments("release-group", releaseGroupId)
                parameter(INCLUDE, include)
            }
        }.body()
    }

    override suspend fun lookupSeries(
        seriesId: String,
        include: String?,
    ): SeriesMusicBrainzNetworkModel {
        return httpClient.get {
            url {
                appendPathSegments("series", seriesId)
                parameter(INCLUDE, include)
            }
        }.body()
    }

    override suspend fun lookupWork(
        workId: String,
        include: String?,
    ): WorkMusicBrainzNetworkModel {
        return httpClient.get {
            url {
                appendPathSegments("work", workId)
                parameter(INCLUDE, include)
            }
        }.body()
    }

    // https://musicbrainz.org/doc/MusicBrainz_API#url_(by_text)
    // example url linked to multiple entities: https://musicbrainz.org/ws/2/url?limit=1&fmt=json&inc=artist-rels+release-rels+recording-rels&resource=https://open.spotify.com/artist/7wcH6naXfssACcXRregV1H
    override suspend fun lookupUrl(
        url: String,
    ): UrlMusicBrainzNetworkModel {
        return httpClient.get {
            url {
                appendPathSegments("url")
                parameter("limit", 1)
                parameter(
                    key = INCLUDE,
                    value = relatableEntities
                        .minus(MusicBrainzEntityType.URL)
                        .asRelationshipParameters,
                )
                parameter("resource", url)
            }
        }.body()
    }
}

val Iterable<MusicBrainzEntityType>.asRelationshipParameters: String
    get() {
        return this.joinToString(separator = "+") { "${it.resourceUri}-rels" }
    }
