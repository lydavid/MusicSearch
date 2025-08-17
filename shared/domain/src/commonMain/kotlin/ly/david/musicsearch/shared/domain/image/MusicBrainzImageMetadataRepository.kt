package ly.david.musicsearch.shared.domain.image

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.artist.ArtistImageRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

/**
 * See [ArtistImageRepository] for artists.
 */
interface MusicBrainzImageMetadataRepository {
    /**
     * Returns metadata for an image, including its url. An image that does not exist will have an empty url.
     *
     * Also saves it to db.
     *
     * Appropriate for getting a single image in a details view.
     */
    suspend fun getAndSaveImageMetadata(
        mbid: String,
        entity: MusicBrainzEntityType,
        forceRefresh: Boolean,
    ): ImageMetadataWithCount

    /**
     * Saves metadata for an image, eventually. For performance reasons, we will batch the write to the database.
     *
     * Appropriate for getting images in a list view, where each item will contain its own image metadata.
     *
     * @param itemsCount How many list item there are. We use this to determine whether we should batch the write.
     */
    suspend fun saveImageMetadata(
        mbid: String,
        entity: MusicBrainzEntityType,
        itemsCount: Int,
    )

    /**
     * [sortOption] is ignored when [mbid] is provided.
     */
    fun observeAllImageMetadata(
        mbid: String?,
        query: String,
        sortOption: ImagesSortOption,
    ): Flow<PagingData<ImageMetadata>>

    fun observeCountOfAllImageMetadata(): Flow<Long>
}
