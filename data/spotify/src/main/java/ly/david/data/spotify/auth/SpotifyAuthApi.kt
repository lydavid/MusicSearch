package ly.david.data.spotify.auth

import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

private const val SPOTIFY_AUTH_BASE_URL = "https://accounts.spotify.com/"
private const val CLIENT_CREDENTIALS = "client_credentials"

interface SpotifyAuthApi {

    @POST("api/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = CLIENT_CREDENTIALS
    ): SpotifyAccessToken
}

interface SpotifyAuthApiImpl {
    companion object {
        fun create(builder: Retrofit.Builder): SpotifyAuthApi {
            val retrofit = builder
                .baseUrl(SPOTIFY_AUTH_BASE_URL)
                .build()

            return retrofit.create(SpotifyAuthApi::class.java)
        }
    }
}
