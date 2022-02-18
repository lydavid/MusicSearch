package ly.david.musicbrainzjetpackcompose.data.lookup

import ly.david.musicbrainzjetpackcompose.data.ReleaseGroup
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

    // TODO: watch out for: Note that the number of linked entities returned is always limited to 25. If you need the remaining results, you will have to perform a browse request.
    @GET("release-group/{releaseGroupId}")
    suspend fun lookupReleaseGroup(
        @Path("releaseGroupId") releaseGroupId: String,
        @Query("inc") include: String = "releases+artists+media"
    ): ReleaseGroup
}
