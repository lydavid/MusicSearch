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
    suspend fun getReleaseImageUrl(
        releaseId: String,
        thumbnail: Boolean,
        forceRefresh: Boolean,
    ): Pair<String, Long>

    fun getAllUrlsById(
        mbid: String,
        query: String,
    ): List<ImageUrls>

    fun getNumberOfImagesById(mbid: String): Int
}
