package ly.david.musicsearch.shared.feature.listens

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.paging.cachedIn
import app.cash.paging.PagingData
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.auth.ListenBrainzStore
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class ListensPresenter(
    private val navigator: Navigator,
    private val listenBrainzStore: ListenBrainzStore,
    private val listensListRepository: ListensListRepository,
) : Presenter<ListensUiState> {
    @Composable
    override fun present(): ListensUiState {
        val username by listenBrainzStore.username.collectAsRetainedState("")
        var textFieldText by rememberSaveable { mutableStateOf("") }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        val lazyListState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        val totalCountOfListens by
            listensListRepository.observeUnfilteredCountOfListensByUser(username).collectAsRetainedState(null)
        val listens: Flow<PagingData<ListenListItemModel>> by rememberRetained(username, query) {
            mutableStateOf(
                listensListRepository.observeListens(
                    username = username,
                    query = query,
                )
                    .distinctUntilChanged()
                    .cachedIn(coroutineScope),
            )
        }

        fun eventSink(event: ListensUiEvent) {
            when (event) {
                is ListensUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is ListensUiEvent.EditText -> {
                    textFieldText = event.text
                }

                is ListensUiEvent.SetUsername -> {
                    coroutineScope.launch {
                        listenBrainzStore.setUsername(textFieldText)
                        lazyListState.scrollToItem(0)
                    }
                }

                is ListensUiEvent.ClickItem -> {
                    navigator.goTo(
                        DetailsScreen(
                            entity = event.entity,
                            id = event.id,
                        ),
                    )
                }
            }
        }

        return ListensUiState(
            username = username,
            textFieldText = textFieldText,
            totalCountOfListens = totalCountOfListens,
            topAppBarFilterState = topAppBarFilterState,
            lazyListState = lazyListState,
            listensPagingDataFlow = listens,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class ListensUiState(
    val username: String = "",
    val textFieldText: String = "",
    val totalCountOfListens: Long? = null,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val lazyListState: LazyListState = LazyListState(),
    val listensPagingDataFlow: Flow<PagingData<ListenListItemModel>> = emptyFlow(),
    val eventSink: (ListensUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface ListensUiEvent : CircuitUiEvent {
    data object NavigateUp : ListensUiEvent

    data class EditText(
        val text: String,
    ) : ListensUiEvent

    data object SetUsername : ListensUiEvent

    data class ClickItem(
        val entity: MusicBrainzEntityType,
        val id: String,
    ) : ListensUiEvent
}
