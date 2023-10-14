package ly.david.data.spotify.api.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters

private const val SPOTIFY_AUTH = "https://accounts.spotify.com/api/token"
private const val CLIENT_CREDENTIALS = "client_credentials"

interface SpotifyOAuthApi {

    companion object {
        fun create(
            httpClient: HttpClient,
        ): SpotifyOAuthApi {
            return SpotifyOAuthApiImpl(
                httpClient = httpClient,
            )
        }
    }

    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String = CLIENT_CREDENTIALS,
    ): SpotifyOAuthClientCredentialsResponse
}

class SpotifyOAuthApiImpl(
    private val httpClient: HttpClient,
) : SpotifyOAuthApi {
    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String,
    ): SpotifyOAuthClientCredentialsResponse {
        return httpClient.submitForm(
            url = SPOTIFY_AUTH,
            formParameters = parameters {
                append("client_id", clientId)
                append("client_secret", clientSecret)
                append("grant_type", grantType)
            },
        ).body()
    }
}
