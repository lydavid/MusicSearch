package ly.david.musicsearch.android.feature.spotify.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.spotify.SpotifyHistoryRepository
import ly.david.musicsearch.ui.common.screen.SearchScreen

internal class SpotifyHistoryPresenter(
    private val navigator: Navigator,
    private val spotifyHistoryRepository: SpotifyHistoryRepository,
) : Presenter<SpotifyUiState> {

    @Composable
    override fun present(): SpotifyUiState {
        var query by rememberSaveable { mutableStateOf("") }
        val lazyPagingItems: LazyPagingItems<ListItemModel> = spotifyHistoryRepository.observeSpotifyHistory(
            query = query,
        ).collectAsLazyPagingItems()

        fun eventSink(event: SpotifyUiEvent) {
            when (event) {
                is SpotifyUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is SpotifyUiEvent.UpdateQuery -> {
                    query = event.query
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

                is SpotifyUiEvent.MarkAsDeleted -> {
                    spotifyHistoryRepository.markAsDeleted(
                        trackId = event.history.trackId,
                        listened = event.history.lastListened,
                    )
                }

                is SpotifyUiEvent.UndoMarkAsDeleted -> {
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
            query = query,
            lazyPagingItems = lazyPagingItems,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class SpotifyUiState(
    val query: String,
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val eventSink: (SpotifyUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface SpotifyUiEvent : CircuitUiEvent {
    data object NavigateUp : SpotifyUiEvent
    data class UpdateQuery(val query: String) : SpotifyUiEvent
    data class GoToSearch(
        val query: String,
        val entity: MusicBrainzEntity,
    ) : SpotifyUiEvent

    data class MarkAsDeleted(val history: SpotifyHistoryListItemModel) : SpotifyUiEvent
    data class UndoMarkAsDeleted(val history: SpotifyHistoryListItemModel) : SpotifyUiEvent
    data class Delete(val history: SpotifyHistoryListItemModel) : SpotifyUiEvent
}
