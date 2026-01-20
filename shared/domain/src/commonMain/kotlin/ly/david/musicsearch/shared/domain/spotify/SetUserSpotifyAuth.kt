package ly.david.musicsearch.shared.domain.spotify

interface SetUserSpotifyAuth {
    suspend operator fun invoke(
        clientId: String,
        clientSecret: String,
    ): Boolean
}
