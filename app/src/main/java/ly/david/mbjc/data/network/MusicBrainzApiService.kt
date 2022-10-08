package ly.david.mbjc.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val MUSIC_BRAINZ_API_BASE_URL = "$MUSIC_BRAINZ_BASE_URL/ws/2/"

/**
 * Contract for MusicBrainz API.
 */
internal interface MusicBrainzApiService : Search, Browse, Lookup

/**
 * Implementation of MusicBrainz API.
 */
internal interface MusicBrainzApiServiceImpl : MusicBrainzApiService {
    companion object {
        fun create(client: OkHttpClient): MusicBrainzApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(JsonUtils.moshi))
                // TODO: should process 503 errors, then we should display alert saying we ran into it (rate limited etc)
                .client(client)
                .baseUrl(MUSIC_BRAINZ_API_BASE_URL)
                .build()

            return retrofit.create(MusicBrainzApiService::class.java)
        }
    }
}
