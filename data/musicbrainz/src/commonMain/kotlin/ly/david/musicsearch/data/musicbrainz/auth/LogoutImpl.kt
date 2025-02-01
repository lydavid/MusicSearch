package ly.david.musicsearch.data.musicbrainz.auth

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzUserApi
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.auth.usecase.Logout

internal class LogoutImpl(
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzUserApi: MusicBrainzUserApi,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    private val logger: Logger,
) : Logout {
    override suspend operator fun invoke() {
        val refreshToken = musicBrainzAuthStore.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) return
        try {
            musicBrainzUserApi.logout(
                token = refreshToken,
                clientId = musicBrainzOAuthInfo.clientId,
                clientSecret = musicBrainzOAuthInfo.clientSecret,
            )
        } catch (ex: Exception) {
            logger.e(ex)
        } finally {
            musicBrainzAuthStore.saveTokens(
                accessToken = "",
                refreshToken = "",
            )
            musicBrainzAuthStore.setUsername("")
        }
    }
}
