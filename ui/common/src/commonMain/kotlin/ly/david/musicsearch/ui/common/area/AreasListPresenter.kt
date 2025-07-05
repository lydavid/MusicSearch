package ly.david.musicsearch.ui.common.area

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
import ly.david.musicsearch.shared.domain.area.AreasListRepository
import ly.david.musicsearch.shared.domain.area.usecase.GetAreas
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver

class AreasListPresenter(
    private val getAreas: GetAreas,
    private val areasListRepository: AreasListRepository,
) : Presenter<AreasListUiState> {
    @Composable
    override fun present(): AreasListUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val pagingDataFlow: Flow<PagingData<ListItemModel>> by rememberRetained(query, browseMethod) {
            mutableStateOf(
                getAreas(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                    ),
                ),
            )
        }
        val count by areasListRepository.observeCountOfAreas(
            browseMethod = browseMethod,
        ).collectAsRetainedState(0)
        val lazyListState = rememberLazyListState()

        return AreasListUiState(
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
        event: AreasListUiEvent,
        onBrowseMethodChanged: (BrowseMethod) -> Unit,
        onIsRemoteChanged: (Boolean) -> Unit,
        onQueryChanged: (String) -> Unit,
    ) {
        when (event) {
            is AreasListUiEvent.Get -> {
                onBrowseMethodChanged(event.browseMethod)
                onIsRemoteChanged(event.isRemote)
            }
            is AreasListUiEvent.UpdateQuery -> {
                onQueryChanged(event.query)
            }
        }
    }
}

sealed interface AreasListUiEvent : CircuitUiEvent {
    data class Get(
        val browseMethod: BrowseMethod,
        val isRemote: Boolean = true,
    ) : AreasListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : AreasListUiEvent
}

@Stable
data class AreasListUiState(
    val pagingDataFlow: Flow<PagingData<ListItemModel>> = emptyFlow(),
    val count: Int = 0,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (AreasListUiEvent) -> Unit = {},
) : CircuitUiState
