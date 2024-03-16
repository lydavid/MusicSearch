package ly.david.musicsearch.android.feature.nowplaying.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.domain.nowplaying.usecase.DeleteNowPlayingHistory
import ly.david.musicsearch.domain.nowplaying.usecase.GetNowPlayingHistory
import ly.david.ui.common.screen.SearchScreen

internal class NowPlayingHistoryPresenter(
    private val navigator: Navigator,
    private val getNowPlayingHistory: GetNowPlayingHistory,
    private val deleteNowPlayingHistory: DeleteNowPlayingHistory,
) : Presenter<NowPlayingHistoryUiState> {

    @Composable
    override fun present(): NowPlayingHistoryUiState {
        var query by rememberSaveable { mutableStateOf("") }
        val lazyPagingItems: LazyPagingItems<ListItemModel> = getNowPlayingHistory(
            query = query,
        ).collectAsLazyPagingItems()

        fun eventSink(event: NowPlayingHistoryUiEvent) {
            when (event) {
                is NowPlayingHistoryUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is NowPlayingHistoryUiEvent.UpdateQuery -> {
                    query = event.query
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
            query = query,
            lazyPagingItems = lazyPagingItems,
            eventSink = ::eventSink,
        )
    }
}
