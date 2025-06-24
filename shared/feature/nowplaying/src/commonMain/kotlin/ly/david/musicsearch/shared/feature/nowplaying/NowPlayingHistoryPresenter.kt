package ly.david.musicsearch.shared.feature.nowplaying

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.nowplaying.usecase.DeleteNowPlayingHistory
import ly.david.musicsearch.shared.domain.nowplaying.usecase.GetNowPlayingHistory
import ly.david.musicsearch.ui.common.screen.SearchScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class NowPlayingHistoryPresenter(
    private val navigator: Navigator,
    private val getNowPlayingHistory: GetNowPlayingHistory,
    private val deleteNowPlayingHistory: DeleteNowPlayingHistory,
) : Presenter<NowPlayingHistoryUiState> {

    @Composable
    override fun present(): NowPlayingHistoryUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        val listItems by rememberRetained(query) {
            mutableStateOf(
                getNowPlayingHistory(
                    query = query,
                ),
            )
        }
        val lazyListState = rememberLazyListState()

        fun eventSink(event: NowPlayingHistoryUiEvent) {
            when (event) {
                is NowPlayingHistoryUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is NowPlayingHistoryUiEvent.DeleteHistory -> {
                    deleteNowPlayingHistory(event.id)
                }

                is NowPlayingHistoryUiEvent.GoToSearch -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            SearchScreen(
                                query = event.query,
                                entity = event.entity,
                            ),
                        ),
                    )
                }
            }
        }

        return NowPlayingHistoryUiState(
            topAppBarFilterState = topAppBarFilterState,
            lazyPagingItems = listItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class NowPlayingHistoryUiState(
    val topAppBarFilterState: TopAppBarFilterState,
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val lazyListState: LazyListState,
    val eventSink: (NowPlayingHistoryUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface NowPlayingHistoryUiEvent : CircuitUiEvent {
    data object NavigateUp : NowPlayingHistoryUiEvent
    data class DeleteHistory(val id: String) : NowPlayingHistoryUiEvent
    data class GoToSearch(
        val query: String,
        val entity: MusicBrainzEntity,
    ) : NowPlayingHistoryUiEvent
}
