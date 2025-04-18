package ly.david.musicsearch.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters
import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_BASE_URL
import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_OAUTH_TOKEN_URL
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo
import ly.david.musicsearch.data.musicbrainz.models.user.UserInfo
import ly.david.musicsearch.shared.domain.APPLICATION_ID

internal const val USER_INFO = "userinfo"

interface MusicBrainzUserApi {

    suspend fun getTokens(
        authCode: String,
        musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    ): TokenResponse

    suspend fun getUserInfo(): UserInfo

    suspend fun logout(
        token: String,
        clientId: String,
        clientSecret: String,
    )
}

interface MusicBrainzUserApiImpl : MusicBrainzUserApi {
    val httpClient: HttpClient

    override suspend fun getTokens(
        authCode: String,
        musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    ): TokenResponse {
        return httpClient.submitForm(
            url = MUSIC_BRAINZ_OAUTH_TOKEN_URL,
            formParameters = parameters {
                append("code", authCode)
                append("grant_type", "authorization_code")
                append("redirect_uri", "$APPLICATION_ID://oauth2/redirect")
            },
            block = {
                basicAuth(
                    username = musicBrainzOAuthInfo.clientId,
                    password = musicBrainzOAuthInfo.clientSecret,
                )
            },
        ).body()
    }

    override suspend fun getUserInfo(): UserInfo {
        return httpClient.get("$MUSIC_BRAINZ_BASE_URL/oauth2/$USER_INFO").body()
    }

    override suspend fun logout(
        token: String,
        clientId: String,
        clientSecret: String,
    ) {
        httpClient.submitForm(
            url = "$MUSIC_BRAINZ_BASE_URL/oauth2/revoke",
            formParameters = parameters {
                append("token", token)
                append("client_id", clientId)
                append("client_secret", clientSecret)
            },
        )
    }
}
