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
import ly.david.musicsearch.shared.domain.list.FacetListItem
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.ListensScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntity
import ly.david.musicsearch.ui.common.topappbar.toTab

internal class ListensPresenter(
    private val screen: ListensScreen,
    private val navigator: Navigator,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    private val listensListRepository: ListensListRepository,
    private val listenBrainzRepository: ListenBrainzRepository,
) : Presenter<ListensUiState> {
    @Suppress("CyclomaticComplexMethod")
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

        var selectedEntityFacet by rememberSaveable { mutableStateOf(screen.entityFacet) }
        val facetFilterState = rememberTopAppBarFilterState(
            transitionType = TopAppBarFilterState.TransitionType.Horizontal,
        )
        val facetQuery = facetFilterState.filterText
        var selectedTab by rememberSaveable { mutableStateOf(screen.entityFacet?.type?.toTab() ?: Tab.RECORDINGS) }

        val topAppBarFilterState = rememberTopAppBarFilterState()
        val listensQuery = topAppBarFilterState.filterText
        var hasReachedOldest by remember(browseUsername) { mutableStateOf(false) }
        var hasReachedLatest by remember(browseUsername) { mutableStateOf(false) }
        val listens: Flow<PagingData<Identifiable>> by rememberRetained(
            browseUsername,
            listensQuery,
            selectedEntityFacet,
        ) {
            mutableStateOf(
                listensListRepository.observeListens(
                    username = browseUsername,
                    query = listensQuery,
                    entityFacet = selectedEntityFacet,
                    stopPrepending = hasReachedLatest,
                    stopAppending = hasReachedOldest,
                    onReachedLatest = { hasReachedLatest = it },
                ) { hasReachedOldest = it },
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
                    facetFilterState.updateFilterText(event.query)
                }

                is ListensUiEvent.UpdateFacetTab -> {
                    selectedTab = event.tab
                }

                is ListensUiEvent.ToggleFacet -> {
                    val newEntityFacet = event.entityFacet
                    selectedEntityFacet = if (selectedEntityFacet == newEntityFacet) {
                        null
                    } else {
                        newEntityFacet
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
            facetsUiState = FacetsUiState(
                selectedEntityFacet = selectedEntityFacet,
                selectedTab = selectedTab,
                filterState = facetFilterState,
                facetsPagingDataFlow = listensListRepository.observeFacets(
                    entityType = selectedTab.toMusicBrainzEntity() ?: MusicBrainzEntityType.RECORDING,
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
    val facetsUiState: FacetsUiState = FacetsUiState(),
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

internal data class FacetsUiState(
    val selectedEntityFacet: MusicBrainzEntity? = null,
    val selectedTab: Tab = Tab.RECORDINGS,
    val filterState: TopAppBarFilterState = TopAppBarFilterState(),
    val facetsPagingDataFlow: Flow<PagingData<FacetListItem>> = emptyFlow(),
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

    data class UpdateFacetTab(
        val tab: Tab,
    ) : ListensUiEvent

    data class ToggleFacet(
        val entityFacet: MusicBrainzEntity,
    ) : ListensUiEvent

    data class SubmitMapping(
        val recordingMessyBrainzId: String,
        val recordingMusicBrainzId: String,
    ) : ListensUiEvent

    data class RefreshMapping(
        val recordingMessyBrainzId: String,
    ) : ListensUiEvent
}
