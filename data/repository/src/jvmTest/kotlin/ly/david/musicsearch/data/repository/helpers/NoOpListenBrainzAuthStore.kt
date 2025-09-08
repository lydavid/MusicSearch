package ly.david.musicsearch.data.repository.helpers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore

open class NoOpListenBrainzAuthStore : ListenBrainzAuthStore {
    override suspend fun getUserToken(): String {
        return ""
    }

    override fun observeUserToken(): Flow<String> {
        return flowOf("")
    }

    override suspend fun setUserToken(token: String) {
        // No-op
    }

    override val username: Flow<String>
        get() = flowOf("")

    override suspend fun setUsername(username: String) {
        // No-op
    }

    override val browseUsername: Flow<String>
        get() = flowOf("")

    override suspend fun setBrowseUsername(username: String) {
        // No-op
    }
}
