package ly.david.musicsearch.data.listenbrainz.api

import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository

private const val LISTEN_BRAINZ_BASE_URL = "https://listenbrainz.org"

class ListenBrainzRepositoryImpl : ListenBrainzRepository {
    override fun getBaseUrl(): String {
        return LISTEN_BRAINZ_BASE_URL
    }
}
