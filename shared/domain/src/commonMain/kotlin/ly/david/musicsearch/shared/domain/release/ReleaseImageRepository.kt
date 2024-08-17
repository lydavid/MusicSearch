package ly.david.musicsearch.shared.domain.release

import ly.david.musicsearch.shared.domain.image.ImageUrls

/**
 * Logic to retrieve release cover art path.
 */
interface ReleaseImageRepository {
    /**
     * Returns a url to the cover art.
     * Empty if none found.
     *
     * Also saves it to db.
     *
     * Make sure to handle non-404 errors at call site.
     */
    suspend fun getReleaseCoverArtUrlsFromNetworkAndSave(
        releaseId: String,
        thumbnail: Boolean,
    ): String

    fun getAllUrls(mbid: String): List<ImageUrls>
}
