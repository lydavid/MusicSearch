package ly.david.musicbrainzjetpackcompose.musicbrainz

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val MUSIC_BRAINZ_BASE_URL = "https://musicbrainz.org/ws/2/"

internal interface MusicBrainzApiService {

    @GET("artist")
    suspend fun queryArtists(@Query("query") query: String): Artists

    companion object {

        private val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("User-Agent", "MusicBrainzJetpackCompose/0.1.0")
                .addHeader("Accept", "application/json")
                // TODO: response in App Inspection is gibberish, is there a header we need to add?
                .build()
            chain.proceed(request)
        }

        private val client = OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            // TODO: should process 503 errors, then we should display alert saying we ran into it (rate limited etc)
            .build()

        fun create(): MusicBrainzApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(
                    MoshiConverterFactory.create(
                        Moshi.Builder()
                            .add(KotlinJsonAdapterFactory())
                            .build()
                    )
                )
                .client(client)
                .baseUrl(MUSIC_BRAINZ_BASE_URL)
                .build()

            return retrofit.create(MusicBrainzApiService::class.java)
        }
    }
}

internal data class Artists(
    @Json(name = "count") val count: Int, // Total hits
    @Json(name = "offset") val offset: Int,
    @Json(name = "artists") val artists: List<Artist> // Max of 25 at a time
)

internal data class Artist(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "sort-name") val sortName: String,
    @Json(name = "type") val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "score") val score: Int,
    @Json(name = "gender") val gender: String? = null,
    @Json(name = "gender-id") val genderId: String? = null,
    @Json(name = "country") val country: String? = null,
    @Json(name = "disambiguation") val disambiguation: String? = null,
    @Json(name = "life-span") val lifeSpan: LifeSpan,
)

internal data class LifeSpan(
    @Json(name = "begin") val begin: String? = null,
    @Json(name = "ended") val ended: Boolean? = null
)
