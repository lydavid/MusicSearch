package ly.david.data.network.api

import retrofit2.Retrofit

private const val MUSIC_BRAINZ_API_BASE_URL = "$MUSIC_BRAINZ_BASE_URL/ws/2/"

interface MusicBrainzApi : SearchApi, BrowseApi, LookupApi, CollectionApi

interface MusicBrainzApiImpl {
    companion object {
        fun create(builder: Retrofit.Builder): MusicBrainzApi {
            val retrofit = builder
                .baseUrl(MUSIC_BRAINZ_API_BASE_URL)
                .build()

            return retrofit.create(MusicBrainzApi::class.java)
        }
    }
}
