package ly.david.musicbrainzjetpackcompose.data

import ly.david.musicbrainzjetpackcompose.common.JsonUtils
import ly.david.musicbrainzjetpackcompose.common.ServiceUtils
import ly.david.musicbrainzjetpackcompose.data.browse.Browse
import ly.david.musicbrainzjetpackcompose.data.lookup.Lookup
import ly.david.musicbrainzjetpackcompose.data.search.Search
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val MUSIC_BRAINZ_BASE_URL = "https://musicbrainz.org/ws/2/"

internal interface MusicBrainzApiService : Search, Browse, Lookup {
    companion object {
        private val client = OkHttpClient().newBuilder()
            .addInterceptor(ServiceUtils.interceptor)
            // TODO: should process 503 errors, then we should display alert saying we ran into it (rate limited etc)
            .build()

        fun create(): MusicBrainzApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(JsonUtils.moshi))
                .client(client)
                .baseUrl(MUSIC_BRAINZ_BASE_URL)
                .build()

            return retrofit.create(MusicBrainzApiService::class.java)
        }
    }
}


