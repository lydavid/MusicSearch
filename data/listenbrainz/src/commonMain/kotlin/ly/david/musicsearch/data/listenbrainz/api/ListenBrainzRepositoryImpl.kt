package ly.david.musicsearch.data.listenbrainz.api

import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository

private const val LISTEN_BRAINZ_BASE_URL = "https://listenbrainz.org"
private const val API_BASE_URL = "https://api.listenbrainz.org/1/"

class ListenBrainzRepositoryImpl : ListenBrainzRepository {
    override fun getBaseUrl(): String {
        return LISTEN_BRAINZ_BASE_URL
    }

    override fun getBaseApiUrl(): String {
        return API_BASE_URL
    }
}
