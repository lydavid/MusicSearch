package ly.david.musicsearch.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthResponse

internal const val REFRESH_TOKEN = "refresh_token"

interface MusicBrainzOAuthApi {

    companion object {
        fun create(
            httpClient: HttpClient,
        ): MusicBrainzOAuthApi {
            return MusicBrainzOAuthApiImpl(
                httpClient = httpClient,
            )
        }
    }

    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String,
        refreshToken: String,
    ): MusicBrainzOAuthResponse
}

class MusicBrainzOAuthApiImpl(
    val httpClient: HttpClient,
) : MusicBrainzOAuthApi {
    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String,
        refreshToken: String,
    ): MusicBrainzOAuthResponse {
        return httpClient.submitForm(
            url = "$MUSIC_BRAINZ_BASE_URL/oauth2/token",
            formParameters = parameters {
                append("client_id", clientId)
                append("client_secret", clientSecret)
                append("grant_type", grantType)
                append("refresh_token", refreshToken)
            },
        ).body()
    }
}
