package ly.david.musicsearch.shared.domain.listen

import kotlinx.coroutines.flow.Flow

interface ListenBrainzAuthStore {
    suspend fun getUserToken(): String
    fun observeUserToken(): Flow<String>
    suspend fun setUserToken(token: String)

    val username: Flow<String>
    suspend fun setUsername(username: String)

    val browseUsername: Flow<String>
    suspend fun setBrowseUsername(username: String)
}
