package ly.david.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState

private const val MB_AUTH_KEY = "musicBrainzAuth"
private val MB_AUTH_PREFERENCE = stringPreferencesKey(MB_AUTH_KEY)

interface MusicBrainzAuthState {
    suspend fun getAuthState(): AuthState?
    fun setAuthState(authState: AuthState?)
}

class MusicBrainzAuthStateImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope
) : MusicBrainzAuthState {

    override suspend fun getAuthState(): AuthState? {
        val preferences = preferencesDataStore.data.first()
        return AuthState.jsonDeserialize(preferences[MB_AUTH_PREFERENCE].orEmpty())
    }

    override fun setAuthState(authState: AuthState?) {
        coroutineScope.launch {
            val serializedAuthState = authState?.jsonSerializeString() ?: return@launch
            preferencesDataStore.edit {
                it[MB_AUTH_PREFERENCE] = serializedAuthState
            }
        }
    }
}
