package ly.david.musicsearch.shared.feature.settings.internal.behaviour

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.preferences.AppPreferences

internal class BehaviourSettingsPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
) : Presenter<BehaviourSettingsUiState> {
    @Composable
    override fun present(): BehaviourSettingsUiState {
        val scrollToHideTopAppBar by appPreferences.scrollToHideTopAppBar.collectAsRetainedState(false)
        val isPullToRefreshEnabled by appPreferences.isPullToRefreshEnabled.collectAsRetainedState(false)
        val confirmChanges by appPreferences.confirmChanges.collectAsRetainedState(true)

        fun eventSink(event: BehaviourSettingsUiEvent) {
            when (event) {
                BehaviourSettingsUiEvent.NavigateUp -> navigator.pop()

                is BehaviourSettingsUiEvent.UpdateScrollToHideTopAppBar -> {
                    appPreferences.setScrollToHideTopAppBar(event.enable)
                }

                is BehaviourSettingsUiEvent.UpdatePullToRefresh -> {
                    appPreferences.setPullToRefresh(event.enable)
                }

                is BehaviourSettingsUiEvent.UpdateConfirmChanges -> {
                    appPreferences.setConfirmChanges(event.confirm)
                }
            }
        }

        return BehaviourSettingsUiState(
            scrollToHideTopAppBar = scrollToHideTopAppBar,
            isPullToRefreshEnabled = isPullToRefreshEnabled,
            confirmChanges = confirmChanges,
            eventSink = ::eventSink,
        )
    }
}

internal data class BehaviourSettingsUiState(
    val scrollToHideTopAppBar: Boolean = false,
    val isPullToRefreshEnabled: Boolean = true,
    val confirmChanges: Boolean = true,
    val eventSink: (BehaviourSettingsUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface BehaviourSettingsUiEvent : CircuitUiEvent {
    data object NavigateUp : BehaviourSettingsUiEvent
    data class UpdateScrollToHideTopAppBar(val enable: Boolean) : BehaviourSettingsUiEvent
    data class UpdatePullToRefresh(val enable: Boolean) : BehaviourSettingsUiEvent
    data class UpdateConfirmChanges(val confirm: Boolean) : BehaviourSettingsUiEvent
}
