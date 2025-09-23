package ly.david.musicsearch.shared.feature.listens

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.recording.RecordingFacet
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.ListensScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class ListensPresenter(
    private val screen: ListensScreen,
    private val navigator: Navigator,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    private val listensListRepository: ListensListRepository,
    private val listenBrainzRepository: ListenBrainzRepository,
) : Presenter<ListensUiState> {
    @Composable
    override fun present(): ListensUiState {
        val loggedInUsername by listenBrainzAuthStore.username.collectAsRetainedState("")
        val browseUsername by listenBrainzAuthStore.browseUsername.collectAsRetainedState("")
        var textFieldText by rememberSaveable { mutableStateOf("") }
        var actionableResult by remember { mutableStateOf<ActionableResult?>(null) }

        val lazyListState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        val totalCountOfListens: Long? by
            listensListRepository.observeUnfilteredCountOfListensByUser(browseUsername).collectAsRetainedState(null)

        var selectedRecordingFacetId by rememberSaveable { mutableStateOf(screen.recordingId) }
        var facetQuery by rememberSaveable { mutableStateOf("") }

        val topAppBarFilterState = rememberTopAppBarFilterState()
        val listensQuery = topAppBarFilterState.filterText
        var hasReachedOldest by remember(browseUsername) { mutableStateOf(false) }
        var hasReachedLatest by remember(browseUsername) { mutableStateOf(false) }
        val listens: Flow<PagingData<Identifiable>> by rememberRetained(
            browseUsername,
            listensQuery,
            selectedRecordingFacetId,
        ) {
            mutableStateOf(
                listensListRepository.observeListens(
                    username = browseUsername,
                    query = listensQuery,
                    recordingId = selectedRecordingFacetId,
                    stopPrepending = hasReachedLatest,
                    stopAppending = hasReachedOldest,
                    onReachedLatest = { hasReachedLatest = it },
                    onReachedOldest = { hasReachedOldest = it },
                ),
            )
        }

        topAppBarFilterState.show(browseUsername.isNotEmpty())

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
                        listenBrainzAuthStore.setBrowseUsername(textFieldText)
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

                is ListensUiEvent.UpdateFacetQuery -> {
                    facetQuery = event.query
                }

                is ListensUiEvent.ToggleRecordingFacet -> {
                    val newId = event.recordingId
                    selectedRecordingFacetId = if (selectedRecordingFacetId == newId) {
                        null
                    } else {
                        newId
                    }
                }

                is ListensUiEvent.SubmitMapping -> {
                    coroutineScope.launch {
                        actionableResult = listensListRepository.submitManualMapping(
                            recordingMessyBrainzId = event.recordingMessyBrainzId,
                            rawRecordingMusicBrainzId = event.recordingMusicBrainzId,
                        )
                    }
                }

                is ListensUiEvent.RefreshMapping -> {
                    coroutineScope.launch {
                        actionableResult = listensListRepository.refreshMapping(
                            recordingMessyBrainzId = event.recordingMessyBrainzId,
                        )
                    }
                }
            }
        }

        return ListensUiState(
            listenBrainzUrl = listenBrainzRepository.getBaseUrl(),
            username = browseUsername,
            textFieldText = textFieldText,
            actionableResult = actionableResult,
            totalCountOfListens = totalCountOfListens,
            topAppBarFilterState = topAppBarFilterState,
            recordingFacetUiState = RecordingFacetUiState(
                selectedRecordingFacetId = selectedRecordingFacetId,
                query = facetQuery,
                recordingFacetsPagingDataFlow = listensListRepository.observeRecordingFacets(
                    username = browseUsername,
                    query = facetQuery,
                ),
            ),
            lazyListState = lazyListState,
            listensPagingDataFlow = listens,
            browsingUserIsSameAsLoggedInUser = loggedInUsername.isNotBlank() && browseUsername == loggedInUsername,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class ListensUiState(
    val listenBrainzUrl: String = "",
    val username: String = "",
    val textFieldText: String = "",
    val actionableResult: ActionableResult? = null,
    val totalCountOfListens: Long? = null,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val recordingFacetUiState: RecordingFacetUiState = RecordingFacetUiState(),
    val lazyListState: LazyListState = LazyListState(),
    val listensPagingDataFlow: Flow<PagingData<Identifiable>> = emptyFlow(),
    val browsingUserIsSameAsLoggedInUser: Boolean = false,
    val eventSink: (ListensUiEvent) -> Unit = {},
) : CircuitUiState {
    val noUsernameSet: Boolean
        get() = username.isEmpty()
    val userListensUrl: String
        get() = "$listenBrainzUrl/user/$username"
}

internal data class RecordingFacetUiState(
    val selectedRecordingFacetId: String? = null,
    val query: String = "",
    val recordingFacetsPagingDataFlow: Flow<PagingData<RecordingFacet>> = emptyFlow(),
)

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

    data class UpdateFacetQuery(
        val query: String,
    ) : ListensUiEvent

    data class ToggleRecordingFacet(
        val recordingId: String,
    ) : ListensUiEvent

    data class SubmitMapping(
        val recordingMessyBrainzId: String,
        val recordingMusicBrainzId: String,
    ) : ListensUiEvent

    data class RefreshMapping(
        val recordingMessyBrainzId: String,
    ) : ListensUiEvent
}
