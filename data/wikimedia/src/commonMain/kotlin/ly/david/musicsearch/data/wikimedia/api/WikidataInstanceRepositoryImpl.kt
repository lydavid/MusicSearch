package ly.david.musicsearch.data.wikimedia.api

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import ly.david.musicsearch.shared.domain.WIKIDATA_BASE_URL
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.preferences.WikidataInstance
import ly.david.musicsearch.shared.domain.wikimedia.WikidataInstanceRepository

private const val API_PATH = "/w/api.php"

class WikidataInstanceRepositoryImpl(
    private val appPreferences: AppPreferences,
) : WikidataInstanceRepository {
    override fun getBaseUrl(): String {
        return runBlocking {
            when (val instance = appPreferences.wikidataInstance.first()) {
                WikidataInstance.Default -> {
                    "${WIKIDATA_BASE_URL}${API_PATH}"
                }

                is WikidataInstance.Custom -> {
                    "${instance.url}${API_PATH}"
                }
            }
        }
    }
}
