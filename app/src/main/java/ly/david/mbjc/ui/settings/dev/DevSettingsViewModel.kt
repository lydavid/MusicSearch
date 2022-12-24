package ly.david.mbjc.ui.settings.dev

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val SHOW_THING = booleanPreferencesKey("show_thing")

@HiltViewModel
internal class DevSettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    // Issue for StateFlow so we don't have to specify defaults twice: https://issuetracker.google.com/issues/196441778
    val showThingFlow: Flow<Boolean>
        get() = dataStore.data
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
            dataStore.edit {
                it[SHOW_THING] = show
            }
        }
    }
}
