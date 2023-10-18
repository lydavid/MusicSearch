package ly.david.data.coverart.api

import io.ktor.client.HttpClient

interface CoverArtArchiveApi {
    /**
     * This is used to get the URLs for this release.
     * So after calling this, we need to make another API call based on the retrieved URLs.
     * The image loading library will generally handle that next call.
     */
    suspend fun getReleaseCoverArts(releaseId: String): CoverArtsResponse

    /**
     * This is used to get the URLs for this release group.
     * So after calling this, we need to make another API call based on the retrieved URLs.
     * The image loading library will generally handle that next call.
     */
    suspend fun getReleaseGroupCoverArts(releaseGroupId: String): CoverArtsResponse

    companion object {
        fun create(
            httpClient: HttpClient,
        ): CoverArtArchiveApi {
            return CoverArtArchiveApiImpl(
                client = httpClient,
            )
        }
    }
}
