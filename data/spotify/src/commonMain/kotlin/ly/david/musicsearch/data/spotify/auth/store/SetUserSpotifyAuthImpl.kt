package ly.david.musicsearch.data.spotify.auth.store

import ly.david.musicsearch.data.spotify.auth.api.SpotifyOAuthApi
import ly.david.musicsearch.shared.domain.spotify.SetUserSpotifyAuth

class SetUserSpotifyAuthImpl(
    private val spotifyAuthStore: SpotifyAuthStore,
    private val spotifyOAuthApi: SpotifyOAuthApi,
) : SetUserSpotifyAuth {

    override suspend fun invoke(
        clientId: String,
        clientSecret: String,
    ): Boolean {
        spotifyAuthStore.setUserSpotifyClientId(clientId)
        spotifyAuthStore.setUserSpotifyClientSecret(clientSecret)

        try {
            // This should be near-instant, and there's no retry.
            val response = spotifyOAuthApi.getAccessToken(
                clientId = clientId,
                clientSecret = clientSecret,
            )
            val accessToken = response.accessToken
            spotifyAuthStore.setAccessToken(
                accessToken = accessToken,
            )
        } catch (_: Exception) {
            spotifyAuthStore.setUserSpotifyClientId("")
            spotifyAuthStore.setUserSpotifyClientSecret("")
            return false
        }

        return true
    }
}
