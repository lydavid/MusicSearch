package ly.david.data.network.api

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
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val AREA_REL = "area-rels"
private const val ARTIST_REL = "artist-rels"
private const val EVENT_REL = "event-rels"
private const val GENRE_REL = "genre-rels"
private const val INSTRUMENT_REL = "instrument-rels"
private const val LABEL_REL = "label-rels"
private const val PLACE_REL = "place-rels"
private const val RECORDING_REL = "recording-rels"
private const val RELEASE_REL = "release-rels"
private const val RELEASE_GROUP_REL = "release-group-rels"
private const val SERIES_REL = "series-rels"
const val URL_REL = "url-rels"
private const val WORK_REL = "work-rels"

/**
 * See [lookup API](https://wiki.musicbrainz.org/MusicBrainz_API#Lookups).
 *
 * Gets information for a specific entity.
 * Can include additional information related to the entity. Max of 25.
 */
interface LookupApi {

    companion object {
        const val INC_ALL_RELATIONS =
            "$AREA_REL+" +
                "$ARTIST_REL+" +
                "$EVENT_REL+" +
                "$GENRE_REL+" +
                "$INSTRUMENT_REL+" +
                "$LABEL_REL+" +
                "$PLACE_REL+" +
                "$RECORDING_REL+" +
                "$RELEASE_REL+" +
                "$RELEASE_GROUP_REL+" +
                "$SERIES_REL+" +
                "$URL_REL+" +
                WORK_REL

        const val INC_ALL_RELATIONS_EXCEPT_URLS =
            "$AREA_REL+" +
                "$ARTIST_REL+" +
                "$EVENT_REL+" +
                "$GENRE_REL+" +
                "$INSTRUMENT_REL+" +
                "$LABEL_REL+" +
                "$PLACE_REL+" +
                "$RECORDING_REL+" +
                "$RELEASE_REL+" +
                "$RELEASE_GROUP_REL+" +
                "$SERIES_REL+" +
                WORK_REL

        const val INC_ALL_RELATIONS_EXCEPT_EVENTS_URLS =
            "$AREA_REL+" +
                "$ARTIST_REL+" +
                "$GENRE_REL+" +
                "$INSTRUMENT_REL+" +
                "$LABEL_REL+" +
                "$PLACE_REL+" +
                "$RECORDING_REL+" +
                "$RELEASE_REL+" +
                "$RELEASE_GROUP_REL+" +
                "$SERIES_REL+" +
                WORK_REL

        // TODO: use this if we decide to split area relations lookup
        const val AREA_DEFAULT_RELS =
            "$AREA_REL+$ARTIST_REL+$EVENT_REL+$GENRE_REL+$INSTRUMENT_REL+$LABEL_REL+$PLACE_REL+$RELEASE_GROUP_REL+$SERIES_REL+$URL_REL+$WORK_REL"
        const val ARTIST_INC_DEFAULT = "$ARTIST_REL+$LABEL_REL+$RELEASE_GROUP_REL+$URL_REL"
        const val EVENT_INC_DEFAULT =
            "$AREA_REL+$ARTIST_REL+$EVENT_REL+$PLACE_REL+$RECORDING_REL+$RELEASE_REL+$RELEASE_GROUP_REL+$SERIES_REL+$URL_REL+$WORK_REL"
        const val WORK_INC_DEFAULT =
            "$AREA_REL+$ARTIST_REL+$EVENT_REL+$GENRE_REL+$INSTRUMENT_REL+$LABEL_REL+$PLACE_REL+$RELEASE_REL+$RELEASE_GROUP_REL+$SERIES_REL+$URL_REL+$WORK_REL"
    }

    // TODO: lookup with all rels might be a bit too much, especially since there's no pagination
    //  It takes 10s to retrieve 7.6MB of data for the city of New York, with 19381 relationships...
    @GET("area/{areaId}")
    suspend fun lookupArea(
        @Path("areaId") areaId: String,

        @Query("inc") include: String? = null,

        // TODO: Separate tab: artists, events, labels, releases, recordings, places, works
        //  we might be able to do paged browse requests for these
        //  place by area id

        // TODO: place-rels doesn't return anything
        //  it isn't enough to get the data on this page: https://musicbrainz.org/area/74e50e58-5deb-4b99-93a2-decbb365c07f/places
    ): AreaMusicBrainzModel

    @GET("artist/{artistId}")
    suspend fun lookupArtist(
        @Path("artistId") artistId: String,
        @Query("inc") include: String? = URL_REL,
    ): ArtistMusicBrainzModel

    @GET("event/{eventId}")
    suspend fun lookupEvent(
        @Path("eventId") eventId: String,
        @Query("inc") include: String? = URL_REL,
    ): EventMusicBrainzModel

    @GET("genre/{genreId}")
    suspend fun lookupGenre(
        @Path("genreId") genreId: String,
        @Query("inc") include: String? = null,
    ): GenreMusicBrainzModel

    @GET("instrument/{instrumentId}")
    suspend fun lookupInstrument(
        @Path("instrumentId") instrumentId: String,
        @Query("inc") include: String = "artist-rels+$URL_REL+area-rels+instrument-rels+genre-rels+label-rels",
    ): InstrumentMusicBrainzModel

    @GET("label/{labelId}")
    suspend fun lookupLabel(
        @Path("labelId") labelId: String,
        @Query("inc") include: String = "artist-rels+label-rels+$URL_REL",
    ): LabelMusicBrainzModel

    @GET("place/{placeId}")
    suspend fun lookupPlace(
        @Path("placeId") placeId: String,
        @Query("inc") include: String? = URL_REL,
    ): PlaceMusicBrainzModel

    @GET("recording/{recordingId}")
    suspend fun lookupRecording(
        @Path("recordingId") recordingId: String,
        @Query("inc") include: String = "artist-credits+$URL_REL",
    ): RecordingMusicBrainzModel

    @GET("release/{releaseId}")
    suspend fun lookupRelease(
        @Path("releaseId") releaseId: String,
        @Query("inc") include: String = "artist-credits" +
            "+labels" + // gives us labels (alternatively, we can get them from rels)
            "+recordings" + // gives us tracks
            "+release-groups" + // gives us types
            "+$URL_REL",
    ): ReleaseMusicBrainzModel

    @GET("release-group/{releaseGroupId}")
    suspend fun lookupReleaseGroup(
        @Path("releaseGroupId") releaseGroupId: String,
        @Query("inc") include: String = "artists+$URL_REL", // "releases+artists+media"
    ): ReleaseGroupMusicBrainzModel

    @GET("series/{seriesId}")
    suspend fun lookupSeries(
        @Path("seriesId") seriesId: String,
        @Query("inc") include: String? = URL_REL,
    ): SeriesMusicBrainzModel

    @GET("work/{workId}")
    suspend fun lookupWork(
        @Path("workId") workId: String,
        @Query("inc") include: String? = URL_REL,
    ): WorkMusicBrainzModel
}
