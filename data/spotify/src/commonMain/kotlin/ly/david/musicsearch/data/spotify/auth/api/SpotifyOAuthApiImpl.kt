package ly.david.musicsearch.data.spotify.auth.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters

private const val SPOTIFY_AUTH = "https://accounts.spotify.com/api/token"

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
                append(
                    name = "client_id",
                    value = clientId,
                )
                append(
                    name = "client_secret",
                    value = clientSecret,
                )
                append(
                    name = "grant_type",
                    value = grantType,
                )
            },
        ).body()
    }
}
