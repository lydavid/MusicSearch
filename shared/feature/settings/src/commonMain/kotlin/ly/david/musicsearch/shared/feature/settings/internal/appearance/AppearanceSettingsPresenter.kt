package ly.david.musicsearch.shared.feature.settings.internal.appearance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.DEFAULT_SEED_COLOR_INT
import ly.david.musicsearch.shared.domain.preferences.AppPreferences

internal class AppearanceSettingsPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
) : Presenter<AppearanceSettingsUiState> {
    @Composable
    override fun present(): AppearanceSettingsUiState {
        val theme by appPreferences.theme.collectAsState(initial = AppPreferences.Theme.SYSTEM)
        val useMaterialYou by appPreferences.useMaterialYou.collectAsState(initial = true)
        val seedColor by appPreferences.observeSeedColor.collectAsState(initial = DEFAULT_SEED_COLOR_INT)

        fun eventSink(event: AppearanceSettingsUiEvent) {
            when (event) {
                AppearanceSettingsUiEvent.NavigateUp -> navigator.pop()

                is AppearanceSettingsUiEvent.UpdateTheme -> {
                    appPreferences.setTheme(event.theme)
                }

                is AppearanceSettingsUiEvent.UpdateUseMaterialYou -> {
                    appPreferences.setUseMaterialYou(event.use)
                }

                is AppearanceSettingsUiEvent.SetSeedColor -> {
                    appPreferences.setSeedColor(event.seedColor)
                }
            }
        }

        return AppearanceSettingsUiState(
            theme = theme,
            useMaterialYou = useMaterialYou,
            seedColor = seedColor,
            eventSink = ::eventSink,
        )
    }
}

internal data class AppearanceSettingsUiState(
    val theme: AppPreferences.Theme = AppPreferences.Theme.SYSTEM,
    val useMaterialYou: Boolean = false,
    val seedColor: Int = DEFAULT_SEED_COLOR_INT,
    val eventSink: (AppearanceSettingsUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface AppearanceSettingsUiEvent : CircuitUiEvent {
    data object NavigateUp : AppearanceSettingsUiEvent
    data class UpdateTheme(val theme: AppPreferences.Theme) : AppearanceSettingsUiEvent
    data class UpdateUseMaterialYou(val use: Boolean) : AppearanceSettingsUiEvent
    data class SetSeedColor(val seedColor: Int) : AppearanceSettingsUiEvent
}
