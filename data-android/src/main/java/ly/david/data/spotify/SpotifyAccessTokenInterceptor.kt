package ly.david.data.spotify

import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import ly.david.data.AUTHORIZATION
import ly.david.data.BEARER
import ly.david.data.BuildConfig
import ly.david.data.spotify.auth.SpotifyAccessToken
import ly.david.data.spotify.auth.SpotifyAuthApi
import okhttp3.Interceptor
import okhttp3.Response

private const val MS_TO_S = 1000

class SpotifyAccessTokenInterceptor @Inject constructor(
    private val spotifyOAuth: SpotifyOAuth,
    private val spotifyAuthApi: SpotifyAuthApi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val hasAuthorizationHeader = originalRequest.headers.names().contains(AUTHORIZATION)
        val accessToken = runBlocking { spotifyOAuth.getAccessToken() }
        val expirationTime = runBlocking { spotifyOAuth.getExpirationTime() } ?: 0L
        if (hasAuthorizationHeader || (!accessToken.isNullOrEmpty() && expirationTime > System.currentTimeMillis())) {
            val requestBuilder = originalRequest.newBuilder()

            if (!accessToken.isNullOrEmpty()) {
                requestBuilder.header(AUTHORIZATION, "$BEARER $accessToken")
            }

            return chain.proceed(requestBuilder.build())
        }

        val newAccessToken = runBlocking { requestNewAccessToken() }
        spotifyOAuth.saveAccessToken(
            accessToken = newAccessToken.accessToken,
            expirationSystemTime = (newAccessToken.expiresIn * MS_TO_S) + System.currentTimeMillis()
        )
        val requestBuilder = originalRequest.newBuilder()
            .header(AUTHORIZATION, "$BEARER ${newAccessToken.accessToken}")

        return chain.proceed(requestBuilder.build())
    }

    private suspend fun requestNewAccessToken(): SpotifyAccessToken {
        return spotifyAuthApi.getAccessToken(
            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
            clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET
        )
    }
}
