package ly.david.musicsearch.data.musicbrainz.auth.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo
import ly.david.musicsearch.shared.domain.musicbrainz.MusicbrainzRepository

class MusicBrainzOAuthApiImpl(
    private val httpClient: HttpClient,
    private val musicbrainzRepository: MusicbrainzRepository,
) : MusicBrainzOAuthApi {
    override suspend fun getAccessToken(
        musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
        grantType: String,
        refreshToken: String,
    ): AccessToken {
        return httpClient.submitForm(
            url = musicbrainzRepository.getTokenEndpoint(),
            formParameters = parameters {
                append(
                    name = "client_id",
                    value = musicBrainzOAuthInfo.clientId,
                )
                append(
                    name = "client_secret",
                    value = musicBrainzOAuthInfo.clientSecret,
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
