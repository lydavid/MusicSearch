package ly.david.musicsearch.ui.common.recording

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
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.recording.RecordingsListRepository
import ly.david.musicsearch.shared.domain.recording.usecase.GetRecordings
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver

class RecordingsListPresenter(
    private val getRecordings: GetRecordings,
    private val recordingsListRepository: RecordingsListRepository,
) : Presenter<RecordingsListUiState> {
    @Composable
    override fun present(): RecordingsListUiState {
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val pagingDataFlow: Flow<PagingData<ListItemModel>> by rememberRetained(browseMethod, query) {
            mutableStateOf(
                getRecordings(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                    ),
                ),
            )
        }
        val count by recordingsListRepository.observeCountOfRecordings(
            browseMethod = browseMethod,
        ).collectAsRetainedState(0)
        val lazyListState: LazyListState = rememberLazyListState()

        fun eventSink(event: RecordingsListUiEvent) {
            when (event) {
                is RecordingsListUiEvent.Get -> {
                    browseMethod = event.browseMethod
                    isRemote = event.isRemote
                }

                is RecordingsListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return RecordingsListUiState(
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
        event: RecordingsListUiEvent,
        onBrowseMethodChanged: (BrowseMethod) -> Unit,
        onIsRemoteChanged: (Boolean) -> Unit,
        onQueryChanged: (String) -> Unit,
    ) {
        when (event) {
            is RecordingsListUiEvent.Get -> {
                onBrowseMethodChanged(event.browseMethod)
                onIsRemoteChanged(event.isRemote)
            }
            is RecordingsListUiEvent.UpdateQuery -> {
                onQueryChanged(event.query)
            }
        }
    }
}

sealed interface RecordingsListUiEvent : CircuitUiEvent {
    data class Get(
        val browseMethod: BrowseMethod,
        val isRemote: Boolean = true,
    ) : RecordingsListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : RecordingsListUiEvent
}

@Stable
data class RecordingsListUiState(
    val pagingDataFlow: Flow<PagingData<ListItemModel>> = emptyFlow(),
    val count: Int = 0,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (RecordingsListUiEvent) -> Unit = {},
) : CircuitUiState
