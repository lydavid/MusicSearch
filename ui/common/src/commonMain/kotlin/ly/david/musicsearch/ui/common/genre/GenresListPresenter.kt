package ly.david.musicsearch.ui.common.genre

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.genre.GenresListRepository
import ly.david.musicsearch.shared.domain.genre.usecase.GetGenres
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver

class GenresListPresenter(
    private val getGenres: GetGenres,
    private val genresListRepository: GenresListRepository,
) : Presenter<GenresListUiState> {
    @Composable
    override fun present(): GenresListUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val pagingDataFlow: Flow<PagingData<ListItemModel>> by rememberRetained(query, browseMethod) {
            mutableStateOf(
                getGenres(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                    ),
                ),
            )
        }
        val count by genresListRepository.observeCountOfGenres(
            browseMethod = browseMethod,
        ).collectAsRetainedState(0)
        val lazyListState: LazyListState = rememberLazyListState()

        fun eventSink(event: GenresListUiEvent) {
            when (event) {
                is GenresListUiEvent.Get -> {
                    browseMethod = event.browseMethod
                    isRemote = event.isRemote
                }

                is GenresListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return GenresListUiState(
            pagingDataFlow = pagingDataFlow,
            count = count,
            lazyListState = lazyListState,
            eventSink = { event ->
                handleEvent(
                    event = event,
                    onBrowseMethodChanged = { browseMethod = it },
                    onIsRemoteChanged = { isRemote = it },
                    onQueryChanged = { query = it },
                )
            },
        )
    }

    private fun handleEvent(
        event: GenresListUiEvent,
        onBrowseMethodChanged: (BrowseMethod) -> Unit,
        onIsRemoteChanged: (Boolean) -> Unit,
        onQueryChanged: (String) -> Unit,
    ) {
        when (event) {
            is GenresListUiEvent.Get -> {
                onBrowseMethodChanged(event.browseMethod)
                onIsRemoteChanged(event.isRemote)
            }
            is GenresListUiEvent.UpdateQuery -> {
                onQueryChanged(event.query)
            }
        }
    }
}

sealed interface GenresListUiEvent : CircuitUiEvent {
    data class Get(
        val browseMethod: BrowseMethod,
        val isRemote: Boolean = true,
    ) : GenresListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : GenresListUiEvent
}

@Stable
data class GenresListUiState(
    val pagingDataFlow: Flow<PagingData<ListItemModel>> = emptyFlow(),
    val count: Int = 0,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (GenresListUiEvent) -> Unit = {},
) : CircuitUiState
