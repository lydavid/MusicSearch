package ly.david.musicsearch.shared.domain.artist

interface ArtistImageRepository {

    /**
     * Returns a url to the artist image.
     * Empty if none found.
     *
     * Also saves it to db.
     */
    suspend fun getArtistImageUrl(
        artistDetailsModel: ArtistDetailsModel,
        forceRefresh: Boolean,
    ): String
}
