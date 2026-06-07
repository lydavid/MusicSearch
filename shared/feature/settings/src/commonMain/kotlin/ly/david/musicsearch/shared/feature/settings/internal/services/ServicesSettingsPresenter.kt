package ly.david.musicsearch.shared.feature.settings.internal.services

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.preferences.MusicBrainzInstance

internal class ServicesSettingsPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
) : Presenter<ServicesSettingsUiState> {
    @Composable
    override fun present(): ServicesSettingsUiState {
        val musicBrainzInstance by appPreferences.musicBrainzInstance.collectAsState(
            initial = MusicBrainzInstance.Default,
        )

        fun eventSink(event: ServicesSettingsUiEvent) {
            when (event) {
                ServicesSettingsUiEvent.NavigateUp -> navigator.pop()

                is ServicesSettingsUiEvent.ConfirmMusicBrainzInstance -> {
                    val instance = if (event.isCustom && event.url.isNotBlank()) {
                        MusicBrainzInstance.Custom(url = event.url)
                    } else {
                        MusicBrainzInstance.Default
                    }
                    appPreferences.setMusicBrainzInstance(instance)
                }

                ServicesSettingsUiEvent.Reset -> {
                    appPreferences.setMusicBrainzInstance(MusicBrainzInstance.Default)
                }
            }
        }

        return ServicesSettingsUiState(
            musicBrainzInstance = musicBrainzInstance,
            eventSink = ::eventSink,
        )
    }
}

internal data class ServicesSettingsUiState(
    val musicBrainzInstance: MusicBrainzInstance,
    val eventSink: (ServicesSettingsUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface ServicesSettingsUiEvent : CircuitUiEvent {
    data object NavigateUp : ServicesSettingsUiEvent

    data class ConfirmMusicBrainzInstance(
        val isCustom: Boolean,
        val url: String,
    ) : ServicesSettingsUiEvent

    data object Reset : ServicesSettingsUiEvent
}
