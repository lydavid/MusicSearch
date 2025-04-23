package ly.david.musicsearch.shared.feature.licenses.internal

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter

internal class LicensesPresenter(
    private val navigator: Navigator,
) : Presenter<LicensesUiState> {
    @Composable
    override fun present(): LicensesUiState {
        fun eventSink(event: LicensesUiEvent) {
            when (event) {
                LicensesUiEvent.NavigateUp -> navigator.pop()
            }
        }

        return LicensesUiState(
            eventSink = ::eventSink,
        )
    }
}

internal sealed interface LicensesUiEvent {
    data object NavigateUp : LicensesUiEvent
}

internal data class LicensesUiState(
    val eventSink: (LicensesUiEvent) -> Unit,
) : CircuitUiState
