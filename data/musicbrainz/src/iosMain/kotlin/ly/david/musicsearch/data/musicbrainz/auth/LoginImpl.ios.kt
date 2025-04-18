package ly.david.musicsearch.data.musicbrainz.auth

import io.ktor.http.Url
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_OAUTH_AUTHORIZATION_URL
import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_OAUTH_SCOPE
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzUserApi
import ly.david.musicsearch.shared.domain.APPLICATION_ID
import ly.david.musicsearch.shared.domain.auth.Login
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import platform.AuthenticationServices.ASPresentationAnchor
import platform.AuthenticationServices.ASWebAuthenticationPresentationContextProvidingProtocol
import platform.AuthenticationServices.ASWebAuthenticationSession
import platform.AuthenticationServices.ASWebAuthenticationSessionCompletionHandler
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoginImpl(
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzUserApi: MusicBrainzUserApi,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    private val logger: Logger,
) : Login {

    // Inspiration from https://github.com/kalinjul/kotlin-multiplatform-oidc/blob/main/oidc-appsupport/src/iosMain/kotlin/org/publicvalue/multiplatform/oidc/appsupport/PlatformCodeAuthFlow.ios.kt
    override suspend operator fun invoke() {
        val requestUrl = NSURL.URLWithString(
            URLString = MUSIC_BRAINZ_OAUTH_AUTHORIZATION_URL +
                "?response_type=code" +
                "&client_id=${musicBrainzOAuthInfo.clientId}" +
                "&redirect_uri=$APPLICATION_ID://oauth2/redirect" +
                "&scope=$MUSIC_BRAINZ_OAUTH_SCOPE",
        ) ?: run {
            error(IllegalStateException("Bad authorization url"))
        }
        val authCode = suspendCoroutine { continuation ->
            val session = ASWebAuthenticationSession(
                uRL = requestUrl,
                callbackURLScheme = Url("$APPLICATION_ID://oauth2/redirect").protocol.name,
                completionHandler = object : ASWebAuthenticationSessionCompletionHandler {
                    override fun invoke(
                        p1: NSURL?,
                        p2: NSError?,
                    ) {
                        if (p1 == null) {
                            continuation.resumeWithException(
                                HandledException(
                                    userMessage = "Cancelled authorization.",
                                    errorResolution = ErrorResolution.None,
                                ),
                            )
                            return
                        }

                        val url = Url(p1.toString())
                        val code = url.parameters["code"]
                        if (code.isNullOrEmpty()) {
                            continuation.resumeWithException(
                                HandledException(
                                    userMessage = "Denied authorization.",
                                    errorResolution = ErrorResolution.None,
                                ),
                            )
                            return
                        }

                        continuation.resume(url.parameters["code"].orEmpty())
                    }
                },
            )
            session.presentationContextProvider = PresentationContext()
            session.start()
        }

        // TODO: generalize token exchange for all platforms
        val response = musicBrainzUserApi.getTokens(
            authCode = authCode,
            musicBrainzOAuthInfo = musicBrainzOAuthInfo,
        )
        musicBrainzAuthStore.saveTokens(
            response.accessToken,
            response.refreshToken,
        )

        try {
            val username = musicBrainzUserApi.getUserInfo().username ?: return
            musicBrainzAuthStore.setUsername(username)
        } catch (ex: Exception) {
            logger.e(ex)
        }
    }

    private class PresentationContext : NSObject(), ASWebAuthenticationPresentationContextProvidingProtocol {
        override fun presentationAnchorForWebAuthenticationSession(
            session: ASWebAuthenticationSession,
        ): ASPresentationAnchor {
            return ASPresentationAnchor()
        }
    }
}
