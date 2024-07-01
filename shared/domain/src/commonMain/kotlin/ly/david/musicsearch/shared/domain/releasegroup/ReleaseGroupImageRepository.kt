package ly.david.musicsearch.shared.domain.releasegroup

/**
 * Logic to retrieve release group cover art path.
 */
interface ReleaseGroupImageRepository {
    /**
     * Returns an appropriate cover art for the release group with [releaseGroupId].
     * Empty if none found.
     *
     * Also saves it to db.
     *
     * Make sure to handle non-404 errors at call site.
     */
    suspend fun getReleaseGroupCoverArtUrlFromNetwork(
        releaseGroupId: String,
        thumbnail: Boolean,
    ): String
}
