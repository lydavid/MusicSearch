package ly.david.data.test.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore

open class NoOpMusicBrainzAuthStore : MusicBrainzAuthStore {
    override suspend fun getAccessToken(): String? {
        return ""
    }

    override val accessToken: Flow<String?>
        get() = flowOf("")

    override suspend fun getRefreshToken(): String? {
        return ""
    }

    override suspend fun getExpiryTimeInEpochSeconds(): Long? {
        return null
    }

    override suspend fun userHasAllAuthScopes(): Boolean {
        return false
    }

    override suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
        scope: String?,
    ) {
        // no-op
    }

    override val username: Flow<String>
        get() = flowOf("")

    override suspend fun setUsername(username: String) {
        // no-op
    }
}
