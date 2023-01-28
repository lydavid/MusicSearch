package ly.david.data.network.api

import ly.david.data.base.JsonUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val MUSIC_BRAINZ_API_BASE_URL = "$MUSIC_BRAINZ_BASE_URL/ws/2/"

/**
 * Contract for MusicBrainz API.
 */
interface MusicBrainzApiService : SearchApi, BrowseApi, LookupApi

/**
 * Implementation of MusicBrainz API.
 */
interface MusicBrainzApiServiceImpl {
    companion object {
        fun create(client: OkHttpClient): MusicBrainzApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(JsonUtils.moshi))
                .client(client)
                .baseUrl(MUSIC_BRAINZ_API_BASE_URL)
                .build()

            return retrofit.create(MusicBrainzApiService::class.java)
        }
    }
}
