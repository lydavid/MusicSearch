package ly.david.musicsearch.data.musicbrainz.auth.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_BASE_URL
import ly.david.musicsearch.core.models.auth.AccessToken

class MusicBrainzOAuthApiImpl(
    val httpClient: HttpClient,
) : MusicBrainzOAuthApi {
    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String,
        refreshToken: String,
    ): AccessToken {
        return httpClient.submitForm(
            url = "$MUSIC_BRAINZ_BASE_URL/oauth2/token",
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
                append(
                    name = "refresh_token",
                    value = refreshToken,
                )
            },
        ).body()
    }
}
