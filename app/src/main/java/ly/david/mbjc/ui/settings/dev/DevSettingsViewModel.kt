package ly.david.mbjc.ui.settings.dev

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val SHOW_THING = booleanPreferencesKey("show_thing")

@HiltViewModel
internal class DevSettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    init {
        viewModelScope.launch {

//            showThingFlow.first()
        }
    }

    // TODO: need to read initial value synchronously: https://stackoverflow.com/a/72986796
    val showThingFlow: Flow<Boolean>
        get() = dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[SHOW_THING] ?: false
            }
            .distinctUntilChanged()

    // TODO: if clicked too fast then it will set it back
    fun setShowThing(show: Boolean) {
        viewModelScope.launch {
            dataStore.edit {
                it[SHOW_THING] = show
            }
        }
    }
}
