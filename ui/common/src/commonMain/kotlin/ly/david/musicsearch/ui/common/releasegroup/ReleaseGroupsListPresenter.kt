package ly.david.musicsearch.ui.common.releasegroup

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.releasegroup.usecase.GetReleaseGroups
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver

class ReleaseGroupsListPresenter(
    private val getReleaseGroups: GetReleaseGroups,
    private val appPreferences: AppPreferences,
    private val imageMetadataRepository: ImageMetadataRepository,
) : Presenter<ReleaseGroupsListUiState> {
    @Composable
    override fun present(): ReleaseGroupsListUiState {
        val scope = rememberCoroutineScope()
        val sorted by appPreferences.sortReleaseGroupListItems.collectAsRetainedState(true)
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        val releaseGroupListItems: Flow<PagingData<ListItemModel>> by rememberRetained(browseMethod, query, sorted) {
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
        val lazyListState: LazyListState = rememberLazyListState()

        fun eventSink(event: ReleaseGroupsListUiEvent) {
            when (event) {
                is ReleaseGroupsListUiEvent.RequestForMissingCoverArtUrl -> {
                    scope.launch {
                        imageMetadataRepository.getImageMetadata(
                            mbid = event.entityId,
                            entity = MusicBrainzEntity.RELEASE_GROUP,
                            forceRefresh = false,
                        )
                    }
                }

                is ReleaseGroupsListUiEvent.UpdateSortReleaseGroupListItem -> {
                    appPreferences.setSortReleaseGroupListItems(event.sort)
                }

                is ReleaseGroupsListUiEvent.Get -> {
                    browseMethod = event.browseMethod
                    isRemote = event.isRemote
                }

                is ReleaseGroupsListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return ReleaseGroupsListUiState(
            lazyPagingItems = releaseGroupListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            sort = sorted,
            eventSink = ::eventSink,
        )
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
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val sort: Boolean,
    val eventSink: (ReleaseGroupsListUiEvent) -> Unit,
) : CircuitUiState
