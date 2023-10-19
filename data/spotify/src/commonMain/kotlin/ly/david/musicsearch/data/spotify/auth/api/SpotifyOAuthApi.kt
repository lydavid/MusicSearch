package ly.david.musicsearch.data.spotify.auth.api

private const val CLIENT_CREDENTIALS = "client_credentials"

interface SpotifyOAuthApi {
    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String = CLIENT_CREDENTIALS,
    ): SpotifyOAuthClientCredentialsResponse
}
