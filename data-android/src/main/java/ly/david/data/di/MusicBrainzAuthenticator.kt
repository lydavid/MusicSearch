package ly.david.data.di

import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import ly.david.data.auth.MusicBrainzAuthState
import ly.david.data.network.api.AUTHORIZATION
import ly.david.data.network.api.BEARER
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class MusicBrainzAuthenticator @Inject constructor(
    private val musicBrainzAuthState: MusicBrainzAuthState,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {

        if (response.request.header(AUTHORIZATION) != null) return null

        return runBlocking {
            val authState = musicBrainzAuthState.getAuthState()

            val builder = response.request.newBuilder()

            val accessToken = authState?.accessToken
            builder.addHeader(AUTHORIZATION, "$BEARER $accessToken")

            builder.build()
        }
    }
}
