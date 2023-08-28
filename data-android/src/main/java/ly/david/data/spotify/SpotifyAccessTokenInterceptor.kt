package ly.david.data.spotify

private const val MS_TO_S = 1000L

//class SpotifyAccessTokenInterceptor @Inject constructor(
//    private val spotifyOAuth: SpotifyOAuth2,
//    private val spotifyAuthApi: SpotifyAuthApi,
//) : Interceptor {
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val originalRequest = chain.request()
//
//        val hasAuthorizationHeader = originalRequest.headers.names().contains(AUTHORIZATION)
//        val accessToken = runBlocking { spotifyOAuth.getAccessToken() }
//        val expirationTime = runBlocking { spotifyOAuth.getExpirationTime() } ?: 0L
//        if (hasAuthorizationHeader || (!accessToken.isNullOrEmpty() && expirationTime > System.currentTimeMillis())) {
//            val requestBuilder = originalRequest.newBuilder()
//
//            if (!accessToken.isNullOrEmpty()) {
//                requestBuilder.header(AUTHORIZATION, "$BEARER $accessToken")
//            }
//
//            return chain.proceed(requestBuilder.build())
//        }
//
//        val newAccessToken = runBlocking { requestNewAccessToken() }
//        spotifyOAuth.saveAccessToken(
//            accessToken = newAccessToken.accessToken,
//            refreshToken = newAccessToken.refreshToken,
//            expirationSystemTime = (newAccessToken.expiresIn * MS_TO_S) + System.currentTimeMillis()
//        )
//        val requestBuilder = originalRequest.newBuilder()
//            .header(AUTHORIZATION, "$BEARER ${newAccessToken.accessToken}")
//
//        return chain.proceed(requestBuilder.build())
//    }
//
//    private suspend fun requestNewAccessToken(): SpotifyAccessToken {
//        return spotifyAuthApi.getAccessToken(
//            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
//            clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET
//        )
//    }
//}
