package ly.david.musicsearch.ui.common.releasegroup

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
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.image.MusicBrainzImageMetadataRepository
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
import ly.david.musicsearch.shared.domain.releasegroup.usecase.GetReleaseGroups
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver

class ReleaseGroupsListPresenter(
    private val getReleaseGroups: GetReleaseGroups,
    private val releaseGroupsListRepository: ReleaseGroupsListRepository,
    private val appPreferences: AppPreferences,
    private val musicBrainzImageMetadataRepository: MusicBrainzImageMetadataRepository,
) : Presenter<ReleaseGroupsListUiState> {
    @Composable
    override fun present(): ReleaseGroupsListUiState {
        val scope = rememberCoroutineScope()
        val sorted by appPreferences.sortReleaseGroupListItems.collectAsRetainedState(true)
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        val pagingDataFlow: Flow<PagingData<ListItemModel>> by rememberRetained(browseMethod, query, sorted) {
            mutableStateOf(
                getReleaseGroups(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                        sorted = sorted,
                        isRemote = isRemote,
                    ),
                ),
            )
        }
        val count by releaseGroupsListRepository.observeCountOfReleaseGroups(
            browseMethod = browseMethod,
        ).collectAsRetainedState(0)
        val lazyListState: LazyListState = rememberLazyListState()
        var requestedImageMetadataForIds: Set<String> by remember { mutableStateOf(setOf()) }

        return ReleaseGroupsListUiState(
            pagingDataFlow = pagingDataFlow,
            count = count,
            lazyListState = lazyListState,
            sort = sorted,
            eventSink = { event ->
                handleEvent(
                    event = event,
                    onBrowseMethodChanged = { browseMethod = it },
                    onIsRemoteChanged = { isRemote = it },
                    onQueryChanged = { query = it },
                    onRequestForMissingCoverArtUrl = { entityId ->
                        if (!requestedImageMetadataForIds.contains(entityId)) {
                            requestedImageMetadataForIds = requestedImageMetadataForIds + setOf(entityId)
                            scope.launch {
                                musicBrainzImageMetadataRepository.saveImageMetadata(
                                    mbid = entityId,
                                    entity = MusicBrainzEntity.RELEASE_GROUP,
                                    itemsCount = count,
                                )
                            }
                        }
                    },
                )
            },
        )
    }

    private fun handleEvent(
        event: ReleaseGroupsListUiEvent,
        onBrowseMethodChanged: (BrowseMethod) -> Unit,
        onIsRemoteChanged: (Boolean) -> Unit,
        onQueryChanged: (String) -> Unit,
        onRequestForMissingCoverArtUrl: (String) -> Unit,
    ) {
        when (event) {
            is ReleaseGroupsListUiEvent.Get -> {
                onBrowseMethodChanged(event.browseMethod)
                onIsRemoteChanged(event.isRemote)
            }
            is ReleaseGroupsListUiEvent.UpdateQuery -> {
                onQueryChanged(event.query)
            }

            is ReleaseGroupsListUiEvent.RequestForMissingCoverArtUrl -> {
                onRequestForMissingCoverArtUrl(event.entityId)
            }

            is ReleaseGroupsListUiEvent.UpdateSortReleaseGroupListItem -> {
                appPreferences.setSortReleaseGroupListItems(event.sort)
            }
        }
    }
}

sealed interface ReleaseGroupsListUiEvent : CircuitUiEvent {
    data class Get(
        val browseMethod: BrowseMethod,
        val isRemote: Boolean = true,
    ) : ReleaseGroupsListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : ReleaseGroupsListUiEvent

    data class UpdateSortReleaseGroupListItem(
        val sort: Boolean,
    ) : ReleaseGroupsListUiEvent

    data class RequestForMissingCoverArtUrl(
        val entityId: String,
    ) : ReleaseGroupsListUiEvent
}

@Stable
data class ReleaseGroupsListUiState(
    val pagingDataFlow: Flow<PagingData<ListItemModel>> = emptyFlow(),
    val count: Int = 0,
    val lazyListState: LazyListState = LazyListState(),
    val sort: Boolean = true,
    val eventSink: (ReleaseGroupsListUiEvent) -> Unit = {},
) : CircuitUiState
