package ly.david.musicsearch.android.feature.spotify.history

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
import ly.david.musicsearch.shared.domain.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.spotify.SpotifyHistoryRepository
import ly.david.musicsearch.ui.common.screen.SearchScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class SpotifyHistoryPresenter(
    private val navigator: Navigator,
    private val spotifyHistoryRepository: SpotifyHistoryRepository,
) : Presenter<SpotifyUiState> {

    @Composable
    override fun present(): SpotifyUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        val listItems by rememberRetained(query) {
            mutableStateOf(
                spotifyHistoryRepository.observeSpotifyHistory(
                    query = query,
                ),
            )
        }
        val lazyListState = rememberLazyListState()

        fun eventSink(event: SpotifyUiEvent) {
            when (event) {
                is SpotifyUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is SpotifyUiEvent.GoToSearch -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            SearchScreen(
                                query = event.query,
                                entity = event.entity,
                            ),
                        ),
                    )
                }

                is SpotifyUiEvent.MarkForDeletion -> {
                    spotifyHistoryRepository.markAsDeleted(
                        trackId = event.history.trackId,
                        listened = event.history.lastListened,
                    )
                }

                is SpotifyUiEvent.UndoMarkForDeletion -> {
                    spotifyHistoryRepository.undoMarkAsDeleted(
                        trackId = event.history.trackId,
                        listened = event.history.lastListened,
                    )
                }

                is SpotifyUiEvent.Delete -> {
                    spotifyHistoryRepository.delete(
                        trackId = event.history.trackId,
                        listened = event.history.lastListened,
                    )
                }
            }
        }

        return SpotifyUiState(
            topAppBarFilterState = topAppBarFilterState,
            lazyPagingItems = listItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class SpotifyUiState(
    val topAppBarFilterState: TopAppBarFilterState,
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val lazyListState: LazyListState,
    val eventSink: (SpotifyUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface SpotifyUiEvent : CircuitUiEvent {
    data object NavigateUp : SpotifyUiEvent
    data class GoToSearch(
        val query: String,
        val entity: MusicBrainzEntity,
    ) : SpotifyUiEvent

    data class MarkForDeletion(val history: SpotifyHistoryListItemModel) : SpotifyUiEvent
    data class UndoMarkForDeletion(val history: SpotifyHistoryListItemModel) : SpotifyUiEvent
    data class Delete(val history: SpotifyHistoryListItemModel) : SpotifyUiEvent
}
