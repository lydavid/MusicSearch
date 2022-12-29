package ly.david.mbjc.ui.experimental

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import java.util.regex.Pattern
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.mbjc.BuildConfig
import ly.david.mbjc.ui.settings.AppPreferences

private val SHOW_THING = booleanPreferencesKey("show_thing")

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExperimentalSettingsViewModel @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>,
    val appPreferences: AppPreferences
) : ViewModel() {

    // TODO: this type of setting should go in AppPreferences
    // Issue for StateFlow so we don't have to specify defaults twice: https://issuetracker.google.com/issues/196441778
    val showThingFlow: Flow<Boolean>
        get() = preferencesDataStore.data
            .catch {
                if (it is IOException) {
                    emit(emptyPreferences())
                }
            }
            .map { preferences ->
                // No type safety.
                preferences[SHOW_THING] ?: false
            }
            .distinctUntilChanged()

    fun setShowThing(show: Boolean) {
        viewModelScope.launch {
            preferencesDataStore.edit {
                it[SHOW_THING] = show
            }
        }
    }

    private val filter: MutableStateFlow<String> = MutableStateFlow("")

    // TODO: can we avoid flow { emit { ?
    private val regex: Flow<Regex> = filter.flatMapLatest { filter ->
        flow {
            emit(Regex(".*(?i)(${Pattern.quote(filter)}).*"))
        }
    }

    val showTheme: Flow<Boolean> =
        regex.flatMapLatest { regex ->
            flow {
                // TODO: should use passed in stringRes
                //  but consider removing this feature and waiting for search support
                //  which was built-in to xml (see Anki Android)
                emit(regex.matches("Theme") || regex.matches("Light") || regex.matches("Dark") || regex.matches("System"))
            }
        }

    val showAppVersion: Flow<Boolean> =
        regex.flatMapLatest { regex ->
            flow {
                emit(regex.matches("App version") || regex.matches(BuildConfig.VERSION_NAME))
            }
        }

    fun setFilter(filterText: String) {
        this.filter.value = filterText
    }
}
