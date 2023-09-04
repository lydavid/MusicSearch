package ly.david.data.musicbrainz.auth

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.musicbrainz.api.MusicBrainzOAuthApi
import ly.david.data.musicbrainz.api.REFRESH_TOKEN

@Singleton
class MusicBrainzAuthRepository @Inject constructor(
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    private val musicBrainzOAuthApi: MusicBrainzOAuthApi,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
) {

    suspend fun getAuthState(): String? {
        val accessToken = musicBrainzAuthStore.getAccessToken()
        if (accessToken.isNullOrEmpty()) return null
        val refreshToken = musicBrainzAuthStore.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) return null

        // Although this would always request for a new access token,
        // it seems like ktor knows to not always execute this
        // only works until we kill the app
        // TODO: store expiry time so we can refresh only when it's passed
        val musicBrainzOAuthResponse = musicBrainzOAuthApi.getAccessToken(
            clientId = musicBrainzOAuthInfo.clientId,
            clientSecret = musicBrainzOAuthInfo.clientSecret,
            grantType = REFRESH_TOKEN,
            refreshToken = refreshToken,
        )
        val newAccessToken = musicBrainzOAuthResponse.accessToken
        val newRefreshToken = musicBrainzOAuthResponse.refreshToken
        musicBrainzAuthStore.saveTokens(
            newAccessToken,
            newRefreshToken,
        )

        return newAccessToken
    }
}
