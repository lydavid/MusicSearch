package ly.david.musicsearch.shared.domain.artist

interface ArtistRepository {
    suspend fun lookupArtistDetails(
        artistId: String,
        forceRefresh: Boolean,
    ): ArtistDetailsModel
}
