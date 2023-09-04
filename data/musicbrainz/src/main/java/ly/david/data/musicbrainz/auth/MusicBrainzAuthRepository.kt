package ly.david.data.musicbrainz.auth

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.Clock
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
        val refreshToken = musicBrainzAuthStore.getRefreshToken()
        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) return null

        val expiryTime = musicBrainzAuthStore.getExpiryTimeInEpochSeconds()
        val hasExpired = (expiryTime ?: 0L) < Clock.System.now().epochSeconds
        return if (hasExpired) {
            val musicBrainzOAuthResponse = musicBrainzOAuthApi.getAccessToken(
                clientId = musicBrainzOAuthInfo.clientId,
                clientSecret = musicBrainzOAuthInfo.clientSecret,
                grantType = REFRESH_TOKEN,
                refreshToken = refreshToken,
            )
            val newAccessToken = musicBrainzOAuthResponse.accessToken
            val newRefreshToken = musicBrainzOAuthResponse.refreshToken
            musicBrainzAuthStore.saveTokens(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken,
            )
            newAccessToken
        } else {
            accessToken
        }
    }
}
