package ly.david.musicsearch.shared.domain.auth

import kotlinx.coroutines.flow.Flow

interface ListenBrainzStore {
    val username: Flow<String>
    suspend fun setUsername(username: String)
}
