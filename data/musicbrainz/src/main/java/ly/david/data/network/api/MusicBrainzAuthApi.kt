package ly.david.data.network.api

import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MusicBrainzAuthApi {
    @GET("userinfo")
    suspend fun getUserInfo(): UserInfo

    @FormUrlEncoded
    @POST("revoke")
    suspend fun logout(
        @Field("token") token: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
    )
}

interface MusicBrainzAuthApiImpl {
    companion object {
        fun create(builder: Retrofit.Builder): MusicBrainzAuthApi {
            val retrofit = builder
                .baseUrl("$MUSIC_BRAINZ_BASE_URL/oauth2/")
                .build()

            return retrofit.create(MusicBrainzAuthApi::class.java)
        }
    }
}
