package ly.david.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.data.common.transformThisIfNotNullOrEmpty
import net.openid.appauth.AuthState

private const val MB_AUTH_KEY = "musicBrainzAuth"
private val MB_AUTH_PREFERENCE = stringPreferencesKey(MB_AUTH_KEY)
private const val MB_USERNAME = "musicBrainzUsername"
private val MB_USERNAME_PREFERENCE = stringPreferencesKey(MB_USERNAME)

interface MusicBrainzAuthState {
    suspend fun getAuthState(): AuthState?
    val authBearer: Flow<String?>
    fun setAuthState(authState: AuthState?)
    val username: Flow<String>
    fun setUsername(username: String)
}

class MusicBrainzAuthStateImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope
) : MusicBrainzAuthState {

    override suspend fun getAuthState(): AuthState? {
        val preferences = preferencesDataStore.data.first()
        val jsonString = preferences[MB_AUTH_PREFERENCE]
        if (jsonString.isNullOrEmpty()) return null
        return AuthState.jsonDeserialize(jsonString)
    }

    override val authBearer: Flow<String?>
        get() = preferencesDataStore.data
            .map { preferences ->
                val jsonString = preferences[MB_AUTH_PREFERENCE]
                if (jsonString.isNullOrEmpty()) return@map null
                val authState = AuthState.jsonDeserialize(jsonString) ?: return@map null
                authState.idToken?.transformThisIfNotNullOrEmpty { "Bearer $it" }
            }
            .distinctUntilChanged()
//    {
//        val preferences = preferencesDataStore.data.first()
//        val jsonString = preferences[MB_AUTH_PREFERENCE]
//        if (jsonString.isNullOrEmpty()) return null
//        val authState = AuthState.jsonDeserialize(jsonString) ?: return null
//        return authState.idToken?
//    }

    override fun setAuthState(authState: AuthState?) {
        coroutineScope.launch {
            val serializedAuthState: String = authState?.jsonSerializeString() ?: ""
            preferencesDataStore.edit {
                it[MB_AUTH_PREFERENCE] = serializedAuthState
            }
        }
    }

    override val username: Flow<String>
        get() = preferencesDataStore.data
            .map {
                it[MB_USERNAME_PREFERENCE].orEmpty()
            }
            .distinctUntilChanged()

    override fun setUsername(username: String) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[MB_USERNAME_PREFERENCE] = username
            }
        }
    }
}
