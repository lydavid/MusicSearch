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
}
