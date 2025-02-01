package ly.david.musicsearch.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzModel

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

        include: String? = URL_REL,

        // TODO: Separate tab: artists, events, labels, releases, recordings, places, works
        //  we might be able to do paged browse requests for these
        //  place by area id

        // TODO: place-rels doesn't return anything
        //  it isn't enough to get the data on this page: https://musicbrainz.org/area/74e50e58-5deb-4b99-93a2-decbb365c07f/places
    ): AreaMusicBrainzModel

    suspend fun lookupArtist(
        artistId: String,
        include: String? = URL_REL,
    ): ArtistMusicBrainzModel

    suspend fun lookupEvent(
        eventId: String,
        include: String? = URL_REL,
    ): EventMusicBrainzModel

    suspend fun lookupGenre(
        genreId: String,
        include: String? = URL_REL,
    ): GenreMusicBrainzModel

    suspend fun lookupInstrument(
        instrumentId: String,
        include: String = URL_REL,
    ): InstrumentMusicBrainzModel

    suspend fun lookupLabel(
        labelId: String,
        include: String = URL_REL,
    ): LabelMusicBrainzModel

    suspend fun lookupPlace(
        placeId: String,
        include: String? = URL_REL,
    ): PlaceMusicBrainzModel

    suspend fun lookupRecording(
        recordingId: String,
        include: String = "artist-credits+$URL_REL",
    ): RecordingMusicBrainzModel

    suspend fun lookupRelease(
        releaseId: String,
        include: String = "artist-credits" +
            "+labels" + // gives us labels (alternatively, we can get them from rels)
            "+recordings" + // gives us tracks
            "+release-groups" + // gives us types
            "+$URL_REL",
    ): ReleaseMusicBrainzModel

    suspend fun lookupReleaseGroup(
        releaseGroupId: String,
        include: String = "artists+$URL_REL", // "releases+artists+media"
    ): ReleaseGroupMusicBrainzModel

    suspend fun lookupSeries(
        seriesId: String,
        include: String? = URL_REL,
    ): SeriesMusicBrainzModel

    suspend fun lookupWork(
        workId: String,
        include: String? = URL_REL,
    ): WorkMusicBrainzModel
}

interface LookupApiImpl : LookupApi {
    val httpClient: HttpClient

    override suspend fun lookupArea(
        areaId: String,
        include: String?,
    ): AreaMusicBrainzModel {
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
    ): ArtistMusicBrainzModel {
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
    ): EventMusicBrainzModel {
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
    ): GenreMusicBrainzModel {
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
    ): InstrumentMusicBrainzModel {
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
    ): LabelMusicBrainzModel {
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
    ): PlaceMusicBrainzModel {
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
    ): RecordingMusicBrainzModel {
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
    ): ReleaseMusicBrainzModel {
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
    ): ReleaseGroupMusicBrainzModel {
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
    ): SeriesMusicBrainzModel {
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
    ): WorkMusicBrainzModel {
        return httpClient.get {
            url {
                appendPathSegments("work", workId)
                parameter("inc", include)
            }
        }.body()
    }
}
