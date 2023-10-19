package ly.david.musicsearch.data.coverart

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
    suspend fun getReleaseCoverArtUrlFromNetwork(
        releaseId: String,
        thumbnail: Boolean,
    ): String
}
