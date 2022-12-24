package ly.david.mbjc.ui.settings

import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.mbjc.R

interface AppPreferences {

    val themeFlow: Flow<Theme>
    fun setTheme(theme: Theme)

    enum class Theme(@StringRes val textRes: Int) {
        LIGHT(R.string.light),
        DARK(R.string.dark),
        SYSTEM(R.string.system),
    }
}

private const val THEME_KEY = "theme"
private val THEME_PREFERENCE = stringPreferencesKey(THEME_KEY)

class AppPreferencesImpl @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope
) : AppPreferences {

    override val themeFlow: Flow<AppPreferences.Theme>
        get() = preferencesDataStore.data
            .map { preferences ->
                AppPreferences.Theme.valueOf(preferences[THEME_PREFERENCE] ?: AppPreferences.Theme.SYSTEM.name)
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
}
