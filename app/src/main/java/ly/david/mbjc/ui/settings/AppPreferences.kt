package ly.david.mbjc.ui.settings

import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.mbjc.R
import net.openid.appauth.AuthState
import timber.log.Timber

interface AppPreferences {

    // TODO: if we wrap authstate with our own, then we move to data-android
    val authState: Flow<AuthState?>
    fun setAuthState(authState: AuthState?)

    enum class Theme(@StringRes val textRes: Int) {
        LIGHT(R.string.light),
        DARK(R.string.dark),
        SYSTEM(R.string.system),
    }

    val theme: Flow<Theme>
    fun setTheme(theme: Theme)

    val useMaterialYou: Flow<Boolean>
    fun setUseMaterialYou(use: Boolean)

    val showMoreInfoInReleaseListItem: Flow<Boolean>
    fun setShowMoreInfoInReleaseListItem(show: Boolean)

}

private const val MB_AUTH_KEY = "musicBrainzAuth"
private val MB_AUTH_PREFERENCE = stringPreferencesKey(MB_AUTH_KEY)

private const val THEME_KEY = "theme"
private val THEME_PREFERENCE = stringPreferencesKey(THEME_KEY)

private const val USE_MATERIAL_YOU_KEY = "useMaterialYou"
private val USE_MATERIAL_YOU_PREFERENCE = booleanPreferencesKey(USE_MATERIAL_YOU_KEY)

private const val SHOW_MORE_INFO_IN_RELEASE_LIST_ITEM_KEY = "showMoreInfoInReleaseListItem"
private val SHOW_MORE_INFO_IN_RELEASE_LIST_ITEM_PREFERENCE =
    booleanPreferencesKey(SHOW_MORE_INFO_IN_RELEASE_LIST_ITEM_KEY)

class AppPreferencesImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope
) : AppPreferences {

    override val authState: Flow<AuthState?>
        get() = preferencesDataStore.data
            .map {
                AuthState.jsonDeserialize(it[MB_AUTH_PREFERENCE].orEmpty())
            }
            .distinctUntilChanged()

    override fun setAuthState(authState: AuthState?) {
        coroutineScope.launch {
            val serializedAuthState = authState?.jsonSerializeString() ?: return@launch
            Timber.d("serializedAuthState=$serializedAuthState")
            preferencesDataStore.edit {
                it[MB_AUTH_PREFERENCE] = serializedAuthState
            }
        }
    }

    override val theme: Flow<AppPreferences.Theme>
        get() = preferencesDataStore.data
            .map {
                AppPreferences.Theme.valueOf(it[THEME_PREFERENCE] ?: AppPreferences.Theme.SYSTEM.name)
            }
            .distinctUntilChanged()

    // If injecting coroutineScope causes issues, we can turn this into suspend and have consumer launch in coroutine
    override fun setTheme(theme: AppPreferences.Theme) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[THEME_PREFERENCE] = theme.name
            }
        }
    }

    override val useMaterialYou: Flow<Boolean>
        get() = preferencesDataStore.data
            .map {
                it[USE_MATERIAL_YOU_PREFERENCE] ?: true
            }
            .distinctUntilChanged()

    override fun setUseMaterialYou(use: Boolean) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[USE_MATERIAL_YOU_PREFERENCE] = use
            }
        }
    }

    override val showMoreInfoInReleaseListItem: Flow<Boolean>
        get() = preferencesDataStore.data
            .map {
                it[SHOW_MORE_INFO_IN_RELEASE_LIST_ITEM_PREFERENCE] ?: true
            }
            .distinctUntilChanged()

    override fun setShowMoreInfoInReleaseListItem(show: Boolean) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[SHOW_MORE_INFO_IN_RELEASE_LIST_ITEM_PREFERENCE] = show
            }
        }
    }
}
