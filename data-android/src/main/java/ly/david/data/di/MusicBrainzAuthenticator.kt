package ly.david.data.di

import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import ly.david.data.auth.MusicBrainzAuthState
import ly.david.data.auth.getBearerToken
import ly.david.data.network.api.AUTHORIZATION
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
            val bearerToken = musicBrainzAuthState.getBearerToken()

            val builder = response.request.newBuilder()
            bearerToken?.let {
                builder.addHeader(AUTHORIZATION, it)
            }
            builder.build()
        }
    }
}
