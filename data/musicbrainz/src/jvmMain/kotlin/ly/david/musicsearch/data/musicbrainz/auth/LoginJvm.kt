package ly.david.musicsearch.data.musicbrainz.auth

import com.github.scribejava.core.model.OAuth2AccessToken
import com.github.scribejava.core.model.OAuthAsyncRequestCallback
import com.github.scribejava.core.oauth.OAuth20Service
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzUserApi

class LoginJvm(
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzUserApi: MusicBrainzUserApi,
    private val logger: Logger,
    private val coroutineScope: CoroutineScope,
    private val oAuth20Service: OAuth20Service,
) {
    operator fun invoke(authCode: String) {
        oAuth20Service.getAccessToken(
            authCode,
            object : OAuthAsyncRequestCallback<OAuth2AccessToken> {
                override fun onCompleted(response: OAuth2AccessToken?) {
                    coroutineScope.launch {
                        musicBrainzAuthStore.saveTokens(
                            response?.accessToken.orEmpty(),
                            response?.refreshToken.orEmpty(),
                        )

                        try {
                            val username = musicBrainzUserApi.getUserInfo().username ?: return@launch
                            musicBrainzAuthStore.setUsername(username)
                        } catch (ex: Exception) {
                            logger.e(ex)
                        }
                    }
                }

                override fun onThrowable(t: Throwable?) {
                    logger.e(Exception(t))
                }
            },
        )
    }
}
