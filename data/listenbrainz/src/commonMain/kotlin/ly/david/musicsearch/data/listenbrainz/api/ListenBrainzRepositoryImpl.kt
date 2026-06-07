package ly.david.musicsearch.data.listenbrainz.api

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import ly.david.musicsearch.shared.domain.LISTEN_BRAINZ_BASE_URL
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.preferences.ListenBrainzInstance

private const val API_BASE_URL = "https://api.listenbrainz.org/1/"

class ListenBrainzRepositoryImpl(
    private val appPreferences: AppPreferences,
) : ListenBrainzRepository {
    override fun getBaseUrl(): String {
        return runBlocking {
            when (val instance = appPreferences.listenBrainzInstance.first()) {
                ListenBrainzInstance.Default -> {
                    LISTEN_BRAINZ_BASE_URL
                }

                is ListenBrainzInstance.Custom -> {
                    instance.url
                }
            }
        }
    }

    override fun getBaseApiUrl(): String {
        return runBlocking {
            when (val instance = appPreferences.listenBrainzInstance.first()) {
                ListenBrainzInstance.Default -> {
                    API_BASE_URL
                }

                is ListenBrainzInstance.Custom -> {
                    "${instance.url}/1/"
                }
            }
        }
    }
}
