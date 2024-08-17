package ly.david.musicsearch.shared.domain.artist

interface ArtistRepository {
    suspend fun lookupArtist(
        artistId: String,
        forceRefresh: Boolean,
    ): ArtistDetailsModel
}
