package ly.david.data.spotify

import ly.david.data.base.JsonUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val SPOTIFY_BASE_URL = "https://api.spotify.com/v1/"

interface SpotifyApi {

    @GET("artists/{artistId}")
    suspend fun getArtist(
        @Path("artistId") artistId: String,
    ): SpotifyArtist
}

interface SpotifyApiImpl {
    companion object {
        fun create(okHttpClient: OkHttpClient): SpotifyApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(JsonUtils.moshi))
                .baseUrl(SPOTIFY_BASE_URL)
                .client(okHttpClient)
                .build()

            return retrofit.create(SpotifyApi::class.java)
        }
    }
}
