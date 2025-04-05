package ly.david.musicsearch.share.feature.database

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class DatabasePresenter(
    private val navigator: Navigator,
) : Presenter<DatabaseUiState> {
    @Composable
    override fun present(): DatabaseUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val lazyListState = rememberLazyListState()

        fun eventSink(event: DatabaseUiEvent) {
            when (event) {
                is DatabaseUiEvent.GoToScreen -> {
                    navigator.goTo(screen = event.screen)
                }
            }
        }

        return DatabaseUiState(
            topAppBarFilterState = topAppBarFilterState,
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class DatabaseUiState(
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (DatabaseUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface DatabaseUiEvent : CircuitUiEvent {
    data class GoToScreen(val screen: Screen) : DatabaseUiEvent
}
