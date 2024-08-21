package ly.david.musicsearch.shared.domain.artist

interface ArtistImageRepository {
    suspend fun getArtistImageUrl(
        artistDetailsModel: ArtistDetailsModel,
        forceRefresh: Boolean,
    ): String
}
