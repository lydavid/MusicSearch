package ly.david.musicsearch.data.musicbrainz.auth

import io.ktor.utils.io.CancellationException
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzUserApi
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.auth.usecase.MusicBrainzLogout

internal class MusicBrainzLogoutImpl(
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzUserApi: MusicBrainzUserApi,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    private val logger: Logger,
) : MusicBrainzLogout {
    override suspend operator fun invoke() {
        val refreshToken = musicBrainzAuthStore.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) return
        try {
            musicBrainzUserApi.logout(
                token = refreshToken,
                clientId = musicBrainzOAuthInfo.clientId,
                clientSecret = musicBrainzOAuthInfo.clientSecret,
            )
        } catch (ex: CancellationException) {
            throw ex
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
