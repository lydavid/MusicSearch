package ly.david.musicsearch.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo
import ly.david.musicsearch.data.musicbrainz.models.user.UserInfo
import ly.david.musicsearch.shared.domain.APPLICATION_ID
import ly.david.musicsearch.shared.domain.musicbrainz.MusicbrainzRepository

internal const val USER_INFO = "userinfo"

interface MusicBrainzUserApi {

    suspend fun getTokens(
        authCode: String,
        codeVerifier: String? = null,
    ): TokenResponse

    suspend fun getUserInfo(): UserInfo

    suspend fun logout(
        token: String,
    )
}

interface MusicBrainzUserApiImpl : MusicBrainzUserApi {
    val httpClient: HttpClient
    val musicBrainzOAuthInfo: MusicBrainzOAuthInfo
    val musicbrainzRepository: MusicbrainzRepository

    override suspend fun getTokens(
        authCode: String,
        codeVerifier: String?,
    ): TokenResponse {
        return httpClient.submitForm(
            url = musicbrainzRepository.getTokenEndpoint(),
            formParameters = parameters {
                append("code", authCode)
                append("grant_type", "authorization_code")
                append("redirect_uri", "$APPLICATION_ID://oauth2/redirect")
                codeVerifier?.let {
                    append("code_verifier", it)
                }
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
        return httpClient.get("${musicbrainzRepository.getOAuthBaseUrl()}/$USER_INFO").body()
    }

    override suspend fun logout(
        token: String,
    ) {
        httpClient.submitForm(
            url = musicbrainzRepository.getRevokeEndpoint(),
            formParameters = parameters {
                append("token", token)
                append("client_id", musicBrainzOAuthInfo.clientId)
                append("client_secret", musicBrainzOAuthInfo.clientSecret)
            },
        )
    }
}
