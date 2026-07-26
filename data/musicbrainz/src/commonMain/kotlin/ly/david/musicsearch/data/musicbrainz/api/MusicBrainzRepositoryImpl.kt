package ly.david.musicsearch.data.musicbrainz.api

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import ly.david.musicsearch.shared.domain.MUSIC_BRAINZ_BASE_URL
import ly.david.musicsearch.shared.domain.musicbrainz.MusicbrainzRepository
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.preferences.MusicBrainzInstance

class MusicBrainzRepositoryImpl(
    private val appPreferences: AppPreferences,
) : MusicbrainzRepository {

    override fun getBaseUrl(): String {
        return runBlocking {
            when (val instance = appPreferences.musicBrainzInstance.first()) {
                MusicBrainzInstance.Default -> {
                    MUSIC_BRAINZ_BASE_URL
                }

                is MusicBrainzInstance.Custom -> {
                    instance.url
                }
            }
        }
    }

    override fun getOAuthBaseUrl(): String {
        return "${getBaseUrl()}/oauth2"
    }

    override fun getAuthorizationEndpoint(): String {
        return "${getOAuthBaseUrl()}/authorize"
    }

    override fun getTokenEndpoint(): String {
        return "${getOAuthBaseUrl()}/token"
    }

    override fun getRevokeEndpoint(): String {
        return "${getOAuthBaseUrl()}/revoke"
    }
}
