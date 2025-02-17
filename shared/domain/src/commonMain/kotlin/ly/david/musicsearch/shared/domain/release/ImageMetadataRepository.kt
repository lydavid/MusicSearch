package ly.david.musicsearch.shared.domain.release

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.coverarts.CoverArtsSortOption
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface ImageMetadataRepository {
    /**
     * Returns a url to the cover art.
     * Empty if none found.
     *
     * Also saves it to db.
     *
     * Make sure to handle non-404 errors at call site.
     */
    suspend fun getImageMetadata(
        mbid: String,
        entity: MusicBrainzEntity,
        forceRefresh: Boolean,
    ): ImageMetadata

    /**
     * [sortOption] is ignored when [mbid] is provided.
     */
    fun observeAllImageMetadata(
        mbid: String?,
        query: String,
        sortOption: CoverArtsSortOption,
    ): Flow<PagingData<ImageMetadata>>

    fun getNumberOfImageMetadataById(mbid: String): Int
}
