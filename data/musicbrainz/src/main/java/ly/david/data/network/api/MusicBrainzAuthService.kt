package ly.david.data.network.api

import ly.david.data.base.JsonUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface MusicBrainzAuthService {
//    @GET("oauth2/authorize")
//    suspend fun authorizeAndRedirect(
//        @Query("response_type") responseType: String = "code",
//        @Query("client_id") clientId: String = MUSIC_BRAINZ_OAUTH_CLIENT_ID,
//        @Query("redirect_uri") redirectUri: String = "io.github.lydavid.musicsearch://oauth2",
//        @Query("scope") scope: String = "collection"
//    )
}

interface MusicBrainzAuthServiceImpl {
    companion object {
        fun create(client: OkHttpClient): MusicBrainzAuthService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(JsonUtils.moshi))
                .client(client)
                .baseUrl(MUSIC_BRAINZ_BASE_URL)
                .build()

            return retrofit.create(MusicBrainzAuthService::class.java)
        }
    }
}
