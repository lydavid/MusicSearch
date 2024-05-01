package ly.david.musicsearch.android.feature.spotify

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import ly.david.musicsearch.core.models.history.SpotifyHistory
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.spotify.SpotifyHistoryRepository
import ly.david.ui.common.screen.SearchScreen

internal class SpotifyHistoryPresenter(
    private val navigator: Navigator,
    private val spotifyHistoryRepository: SpotifyHistoryRepository,
) : Presenter<SpotifyUiState> {

    @Composable
    override fun present(): SpotifyUiState {
        var spotifyPlaying: SpotifyHistory? by remember { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        val lazyPagingItems: LazyPagingItems<ListItemModel> = spotifyHistoryRepository.observeSpotifyHistory(
            query = query,
        ).collectAsLazyPagingItems()

        SpotifyBroadcastReceiver {
            spotifyPlaying = it
            Log.d(
                "findme",
                "$it",
            )
            spotifyHistoryRepository.upsert(it)
        }

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
            }
        }

        return SpotifyUiState(
            query = query,
            lazyPagingItems = lazyPagingItems,
            spotifyHistory = spotifyPlaying,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class SpotifyUiState(
    val spotifyHistory: SpotifyHistory?,
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
}
