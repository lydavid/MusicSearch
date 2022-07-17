package ly.david.mbjc.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * See [lookup API](https://wiki.musicbrainz.org/MusicBrainz_API#Lookups).
 *
 * Gets information for a specific entity.
 * Can include additional information related to the entity. Max of 25.
 */
internal interface Lookup {

    @GET("artist/{artistId}")
    suspend fun lookupArtist(
        @Path("artistId") artistId: String,
//        @Query("inc") include: String = "genres"
    ): ArtistMusicBrainzModel

    @GET("release-group/{releaseGroupId}")
    suspend fun lookupReleaseGroup(
        @Path("releaseGroupId") releaseGroupId: String,
        @Query("inc") include: String = "artists" // "releases+artists+media"
    ): ReleaseGroupMusicBrainzModel

    // TODO: screen should have information similar to: https://musicbrainz.org/release/85363599-44b3-4eb2-b976-382a23d7f1ba
    @GET("release/{releaseId}")
    suspend fun lookupRelease(
        @Path("releaseId") releaseId: String,
//        @Query("inc") include: String = "artist-credits+labels+recordings+recording-level-rels+work-rels+work-level-rels+artist-rels+place-rels+label-rels"
        @Query("inc") include: String = "recordings"
    ): ReleaseMusicBrainzModel

    @GET("recording/{recordingId}")
    suspend fun lookupRecording(
        @Path("recordingId") recordingId: String,
        @Query("inc") include: String = "artist-rels+work-rels+label-rels+place-rels+area-rels+event-rels" +
            "+instrument-rels+recording-rels+release-rels+release-group-rels+series-rels+url-rels" +
            "+artist-credits"
        // "+work-level-rels" // Web displays this in recording screen, but we can reserve it for work screen
    ): RecordingMusicBrainzModel

    // TODO: lookup with all rels might be a bit too much, especially since there's no pagination
    //  It takes 10s to retrieve 7.6MB of data for the city of New York, with 19381 relationships...
    @GET("area/{areaId}")
    suspend fun lookupArea(
        @Path("areaId") areaId: String,

        @Query("inc") include: String =
        // Overview
            "area-rels+url-rels+instrument-rels"

        // TODO: Separate tab: artists, events, labels, releases, recordings, places, works
        //  we might be able to do paged browse requests for these
        //  place by area id

        // TODO: place-rels doesn't return anything
        //  it isn't enough to get the data on this page: https://musicbrainz.org/area/74e50e58-5deb-4b99-93a2-decbb365c07f/places

        //"area-rels+artist-rels+event-rels+instrument-rels+label-rels" +
        //"+place-rels+recording-rels+release-rels+release-group-rels+series-rels+url-rels+work-rels"
    ): AreaMusicBrainzModel

    @GET("place/{placeId}")
    suspend fun lookupPlace(
        @Path("placeId") placeId: String,
        @Query("inc") include: String = "place-rels"//"area-rels+artist-rels+event-rels+instrument-rels+label-rels" +
        //"+place-rels+recording-rels+release-rels+release-group-rels+series-rels+url-rels+work-rels"
    ): PlaceMusicBrainzModel

    @GET("instrument/{instrumentId}")
    suspend fun lookupInstrument(
        @Path("instrumentId") instrumentId: String,
        @Query("inc") include: String = "artist-rels+url-rels+area-rels+instrument-rels+genre-rels"
    ): InstrumentMusicBrainzModel
}
