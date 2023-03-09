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
    private val musicBrainzAuthState: MusicBrainzAuthState
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        return runBlocking {
            val authState = musicBrainzAuthState.getAuthState()
            val token = authState?.accessToken
            response.request.newBuilder()
                .header(AUTHORIZATION, "$BEARER $token")
                .build()
        }
    }
}
