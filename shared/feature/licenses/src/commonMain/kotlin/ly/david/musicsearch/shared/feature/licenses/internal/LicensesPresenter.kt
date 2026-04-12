package ly.david.musicsearch.shared.feature.licenses.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.preferences.AppPreferences

internal class LicensesPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
) : Presenter<LicensesUiState> {
    @Composable
    override fun present(): LicensesUiState {
        val scrollToHideTopAppBar by appPreferences.scrollToHideTopAppBar.collectAsRetainedState(false)

        fun eventSink(event: LicensesUiEvent) {
            when (event) {
                LicensesUiEvent.NavigateUp -> navigator.pop()
            }
        }

        return LicensesUiState(
            scrollToHideTopAppBar = scrollToHideTopAppBar,
            eventSink = ::eventSink,
        )
    }
}

internal sealed interface LicensesUiEvent {
    data object NavigateUp : LicensesUiEvent
}

internal data class LicensesUiState(
    val scrollToHideTopAppBar: Boolean = false,
    val eventSink: (LicensesUiEvent) -> Unit,
) : CircuitUiState
