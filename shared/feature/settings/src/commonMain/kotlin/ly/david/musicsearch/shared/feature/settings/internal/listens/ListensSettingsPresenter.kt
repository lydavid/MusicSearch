package ly.david.musicsearch.shared.feature.settings.internal.listens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.DEFAULT_NUMBER_OF_LATEST_LISTENS_TO_SHOW
import ly.david.musicsearch.shared.domain.preferences.AppPreferences

internal class ListensSettingsPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
) : Presenter<ListensSettingsUiState> {
    @Composable
    override fun present(): ListensSettingsUiState {
        val numberOfListensInDetails by appPreferences.observeNumberOfListensInDetails.collectAsState(
            initial = DEFAULT_NUMBER_OF_LATEST_LISTENS_TO_SHOW,
        )

        fun eventSink(event: ListensSettingsUiEvent) {
            when (event) {
                ListensSettingsUiEvent.NavigateUp -> navigator.pop()

                is ListensSettingsUiEvent.UpdateNumberOfListensInDetails -> {
                    appPreferences.setNumberOfListensInDetails(event.number)
                }
            }
        }

        return ListensSettingsUiState(
            numberOfListensInDetails = numberOfListensInDetails,
            eventSink = ::eventSink,
        )
    }
}

internal data class ListensSettingsUiState(
    val numberOfListensInDetails: Long,
    val eventSink: (ListensSettingsUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface ListensSettingsUiEvent : CircuitUiEvent {
    data object NavigateUp : ListensSettingsUiEvent
    data class UpdateNumberOfListensInDetails(val number: Long) : ListensSettingsUiEvent
}
