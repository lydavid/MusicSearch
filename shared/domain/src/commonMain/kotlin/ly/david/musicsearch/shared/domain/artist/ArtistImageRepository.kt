package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.shared.domain.image.ImageMetadata

interface ArtistImageRepository {

    /**
     * Returns a url to the artist image.
     * Empty if none found.
     *
     * Also saves it to db.
     */
    suspend fun getArtistImageMetadata(
        artistDetailsModel: ArtistDetailsModel,
        forceRefresh: Boolean,
    ): ImageMetadata
}
