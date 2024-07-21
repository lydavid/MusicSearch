package ly.david.musicsearch.shared.domain.artist

interface ArtistImageRepository {
    suspend fun getArtistImageFromNetwork(
        artistMbid: String,
        spotifyUrl: String,
    ): String

    fun deleteImage(
        artistMbid: String,
    )
}
