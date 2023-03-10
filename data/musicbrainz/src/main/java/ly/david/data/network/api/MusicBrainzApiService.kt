package ly.david.data.network.api

import retrofit2.Retrofit

private const val MUSIC_BRAINZ_API_BASE_URL = "$MUSIC_BRAINZ_BASE_URL/ws/2/"

interface MusicBrainzApiService : SearchApi, BrowseApi, LookupApi, CollectionApi

interface MusicBrainzApiServiceImpl {
    companion object {
        fun create(builder: Retrofit.Builder): MusicBrainzApiService {
            val retrofit = builder
                .baseUrl(MUSIC_BRAINZ_API_BASE_URL)
                .build()

            return retrofit.create(MusicBrainzApiService::class.java)
        }
    }
}
