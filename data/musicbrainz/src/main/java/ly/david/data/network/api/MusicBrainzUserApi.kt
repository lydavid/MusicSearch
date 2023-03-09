package ly.david.data.network.api

import com.squareup.moshi.Json
import ly.david.data.base.JsonUtils
import ly.david.data.network.USER_AGENT
import ly.david.data.network.USER_AGENT_VALUE
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

const val MUSIC_BRAINZ_OAUTH_CLIENT_ID = "afBf7jhb_ms-Fjqm6VTWTvVAyuAUf_xT"

/**
 * Mobile apps embed secrets in their code so we don't have to hide this.
 */
const val MUSIC_BRAINZ_OAUTH_CLIENT_SECRET = "nnkNItEfufwKj0-yjgmgZVrnzrXRQBN7"

interface MusicBrainzUserApi {
    @GET("userinfo")
    suspend fun getUserInfo(
//        @Header("Authorization") authHeader: String?
    ): UserInfo

}

interface MusicBrainzUserApiImpl {
    companion object {
        fun create(client: OkHttpClient): MusicBrainzUserApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(JsonUtils.moshi))
                .client(client)
                .baseUrl("$MUSIC_BRAINZ_BASE_URL/oauth2/")
                .build()

            return retrofit.create(MusicBrainzUserApi::class.java)
        }
    }
}

interface MusicBrainzLogoutApi {

    data class LogoutRequest(
        @Json(name = "token") val token: String,
        @Json(name = "client_id") val clientId: String = MUSIC_BRAINZ_OAUTH_CLIENT_ID,
        @Json(name = "client_secret") val clientSecret: String = MUSIC_BRAINZ_OAUTH_CLIENT_SECRET
    )

    @FormUrlEncoded
    @POST("revoke")
    suspend fun logout(
        @Field("token") token: String,
        @Field("client_id") clientId: String = MUSIC_BRAINZ_OAUTH_CLIENT_ID,
        @Field("client_secret") clientSecret: String = MUSIC_BRAINZ_OAUTH_CLIENT_SECRET
    )

    companion object {
        fun create(): MusicBrainzLogoutApi {

            val client = OkHttpClient().newBuilder()
                .addInterceptor { chain ->
                    val request = chain.request()
                    val requestBuilder = request.newBuilder()
                        .addHeader(USER_AGENT, USER_AGENT_VALUE)
//                        .addHeader(ACCEPT, ACCEPT_VALUE)
                    chain.proceed(requestBuilder.build())
                }

                .build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(JsonUtils.moshi))
                .client(client)
                .baseUrl("$MUSIC_BRAINZ_BASE_URL/oauth2/")
                .build()

            return retrofit.create(MusicBrainzLogoutApi::class.java)
        }
    }
}
