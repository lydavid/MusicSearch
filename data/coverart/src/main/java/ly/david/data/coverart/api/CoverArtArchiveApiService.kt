package ly.david.data.coverart.api

import ly.david.data.coverart.COVER_ART_ARCHIVE_BASE_URL
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface CoverArtArchiveApiService {
    /**
     * This is used to get the URLs for this release.
     * So after calling this, we need to make another API call based on the retrieved URLs.
     * The image loading library will generally handle that next call.
     */
    @GET("release/{id}")
    suspend fun getReleaseCoverArts(@Path("id") releaseId: String): CoverArtsResponse

    /**
     * This is used to get the URLs for this release group.
     * So after calling this, we need to make another API call based on the retrieved URLs.
     * The image loading library will generally handle that next call.
     */
    @GET("release-group/{id}")
    suspend fun getReleaseGroupCoverArts(@Path("id") releaseGroupId: String): CoverArtsResponse
}

interface CoverArtArchiveApiServiceImpl {
    companion object {
        fun create(builder: Retrofit.Builder): CoverArtArchiveApiService {
            val retrofit = builder
                .baseUrl(COVER_ART_ARCHIVE_BASE_URL)
                .build()

            return retrofit.create(CoverArtArchiveApiService::class.java)
        }
    }
}
