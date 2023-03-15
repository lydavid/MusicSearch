package ly.david.data.di

import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import ly.david.data.auth.MusicBrainzAuthState
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

private const val AUTHORIZATION = "Authorization"
private const val BEARER = "Bearer"

class MusicBrainzAuthenticator @Inject constructor(
    private val musicBrainzAuthState: MusicBrainzAuthState,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {

        if (response.request.header(AUTHORIZATION) != null) return null

        return runBlocking {
            val authState = musicBrainzAuthState.getAuthState()

            val builder = response.request.newBuilder()

//            suspendCoroutine { continuation ->
//            if (authState?.needsTokenRefresh == true) {
//                authService.performTokenRequest(
//                    authState.createTokenRefreshRequest(),
//                    clientAuth
//                ) { response: TokenResponse?, ex: AuthorizationException? ->
//                    authState.update(response, ex)
//                    musicBrainzAuthState.setAuthState(authState)
//                    builder.addHeader(AUTHORIZATION, "$BEARER ${response?.accessToken}")
//                }
//                authState.performActionWithFreshTokens(
//                    authService,
//                    AuthStateAction { accessToken: String?, idToken: String?, ex: AuthorizationException? ->
//                        Log.d("findme", "$accessToken")
//                        Log.d("findme", "$idToken")
//                        Log.d("findme", "$ex")
//                        // performActionWithFreshTokens is performed on authState, so it should be updated
//                        authState.update(TokenResponse.Builder().build(), ex)
//                        musicBrainzAuthState.setAuthState(authState)
//
//                    }
//                )
//            } else {
            val accessToken = authState?.accessToken
            builder.addHeader(AUTHORIZATION, "$BEARER $accessToken")
//            }
//            }

            builder.build()
        }
    }
}
