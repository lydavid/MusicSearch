package ly.david.mbjc.data.lookup

import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.MusicBrainzReleaseGroup
import ly.david.mbjc.data.Release
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * See [lookup API](https://wiki.musicbrainz.org/MusicBrainz_API#Lookups).
 *
 * Gets information for a specific entity.
 * Can include additional information related to the entity. Max of 25.
 */
interface Lookup {

    @GET("artist/{artistId}")
    suspend fun lookupArtist(
        @Path("artistId") artistId: String,
//        @Query("inc") include: String = "genres"
    ): Artist

    @GET("release-group/{releaseGroupId}")
    suspend fun lookupReleaseGroup(
        @Path("releaseGroupId") releaseGroupId: String,
        @Query("inc") include: String = "releases+artists+media"
    ): MusicBrainzReleaseGroup

    // TODO: screen should have information similar to: https://musicbrainz.org/release/85363599-44b3-4eb2-b976-382a23d7f1ba
    @GET("release/{releaseId}")
    suspend fun lookupRelease(
        @Path("releaseId") releaseId: String,
        @Query("inc") include: String = "artist-credits+labels+recordings+recording-level-rels+work-rels+work-level-rels+artist-rels+place-rels+label-rels"
    ): Release
}
