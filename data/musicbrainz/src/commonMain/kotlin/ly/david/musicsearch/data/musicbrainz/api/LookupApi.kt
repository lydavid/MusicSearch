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
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel

const val URL_REL = "url-rels"

/**
 * See [lookup API](https://wiki.musicbrainz.org/MusicBrainz_API#Lookups).
 *
 * Gets information for a specific entity.
 * Can include additional information related to the entity. Max of 25.
 */
interface LookupApi {

    suspend fun lookupArea(
        areaId: String,

        include: String? = "$URL_REL+$ALIASES",

        // TODO: Separate tab: artists, events, labels, releases, recordings, places, works
        //  we might be able to do paged browse requests for these
        //  place by area id

        // TODO: place-rels doesn't return anything
        //  it isn't enough to get the data on this page: https://musicbrainz.org/area/74e50e58-5deb-4b99-93a2-decbb365c07f/places
    ): AreaMusicBrainzNetworkModel

    suspend fun lookupArtist(
        artistId: String,
        include: String? = "$URL_REL+$ALIASES",
    ): ArtistMusicBrainzNetworkModel

    suspend fun lookupEvent(
        eventId: String,
        include: String? = "$URL_REL+$ALIASES",
    ): EventMusicBrainzNetworkModel

    suspend fun lookupGenre(
        genreId: String,
        include: String? = "$URL_REL+$ALIASES",
    ): GenreMusicBrainzNetworkModel

    suspend fun lookupInstrument(
        instrumentId: String,
        include: String = "$URL_REL+$ALIASES",
    ): InstrumentMusicBrainzNetworkModel

    suspend fun lookupLabel(
        labelId: String,
        include: String = "$URL_REL+$ALIASES",
    ): LabelMusicBrainzNetworkModel

    suspend fun lookupPlace(
        placeId: String,
        include: String? = "$URL_REL+$ALIASES",
    ): PlaceMusicBrainzNetworkModel

    suspend fun lookupRecording(
        recordingId: String,
        include: String = "artist-credits+$URL_REL+$ALIASES",
    ): RecordingMusicBrainzNetworkModel

    suspend fun lookupRelease(
        releaseId: String,
        include: String = "artist-credits" +
            "+labels" + // gives us labels (alternatively, we can get them from rels)
            "+recordings" + // gives us tracks
            "+release-groups" + // gives us types
            "+$URL_REL" +
            "+$ALIASES",
    ): ReleaseMusicBrainzNetworkModel

    suspend fun lookupReleaseGroup(
        releaseGroupId: String,
        include: String = "artists+$URL_REL+$ALIASES", // "releases+artists+media"
    ): ReleaseGroupMusicBrainzNetworkModel

    suspend fun lookupSeries(
        seriesId: String,
        include: String? = "$URL_REL+$ALIASES",
    ): SeriesMusicBrainzNetworkModel

    suspend fun lookupWork(
        workId: String,
        include: String? = "$URL_REL+$ALIASES",
    ): WorkMusicBrainzNetworkModel
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
                parameter("inc", include)
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
                parameter("inc", include)
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
                parameter("inc", include)
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
                parameter("inc", include)
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
                parameter("inc", include)
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
                parameter("inc", include)
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
                parameter("inc", include)
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
                parameter("inc", include)
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
                parameter("inc", include)
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
                parameter("inc", include)
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
                parameter("inc", include)
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
                parameter("inc", include)
            }
        }.body()
    }
}
