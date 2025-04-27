package ly.david.musicsearch.ui.common.series

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.series.usecase.GetSeries
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver

class SeriesListPresenter(
    private val getSeries: GetSeries,
) : Presenter<SeriesListUiState> {
    @Composable
    override fun present(): SeriesListUiState {
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val seriesListItems: Flow<PagingData<ListItemModel>> by rememberRetained(browseMethod, query) {
            mutableStateOf(
                getSeries(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                    ),
                ),
            )
        }
        val lazyListState: LazyListState = rememberLazyListState()

        fun eventSink(event: SeriesListUiEvent) {
            when (event) {
                is SeriesListUiEvent.Get -> {
                    browseMethod = event.browseMethod
                    isRemote = event.isRemote
                }

                is SeriesListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return SeriesListUiState(
            lazyPagingItems = seriesListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface SeriesListUiEvent : CircuitUiEvent {
    data class Get(
        val browseMethod: BrowseMethod,
        val isRemote: Boolean = true,
    ) : SeriesListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : SeriesListUiEvent
}

@Stable
data class SeriesListUiState(
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (SeriesListUiEvent) -> Unit = {},
) : CircuitUiState
