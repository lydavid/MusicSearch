package ly.david.musicsearch.ui.common.release

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
import ly.david.musicsearch.shared.domain.release.ReleasesListRepository
import ly.david.musicsearch.shared.domain.release.usecase.GetReleases
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver

class ReleasesListPresenter(
    private val getReleases: GetReleases,
    private val releasesListRepository: ReleasesListRepository,
    private val appPreferences: AppPreferences,
    private val musicBrainzImageMetadataRepository: MusicBrainzImageMetadataRepository,
) : Presenter<ReleasesListUiState> {
    @Composable
    override fun present(): ReleasesListUiState {
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val pagingDataFlow: Flow<PagingData<ListItemModel>> by rememberRetained(browseMethod, query) {
            mutableStateOf(
                getReleases(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                    ),
                ),
            )
        }
        val releaseCount by releasesListRepository.observeCountOfReleases(
            browseMethod = browseMethod,
        ).collectAsRetainedState(0)
        val lazyListState: LazyListState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val showMoreInfoInReleaseListItem by appPreferences.showMoreInfoInReleaseListItem.collectAsRetainedState(true)
        var requestedImageMetadataForIds: Set<String> by remember { mutableStateOf(setOf()) }

        fun eventSink(event: ReleasesListUiEvent) {
            when (event) {
                is ReleasesListUiEvent.RequestForMissingCoverArtUrl -> {
                    if (!requestedImageMetadataForIds.contains(event.entityId)) {
                        requestedImageMetadataForIds = requestedImageMetadataForIds + setOf(event.entityId)
                        scope.launch {
                            musicBrainzImageMetadataRepository.saveImageMetadata(
                                mbid = event.entityId,
                                entity = MusicBrainzEntity.RELEASE,
                                itemsCount = releaseCount,
                            )
                        }
                    }
                }

                is ReleasesListUiEvent.UpdateShowMoreInfoInReleaseListItem -> {
                    appPreferences.setShowMoreInfoInReleaseListItem(event.showMore)
                }

                is ReleasesListUiEvent.Get -> {
                    browseMethod = event.browseMethod
                    isRemote = event.isRemote
                }

                is ReleasesListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return ReleasesListUiState(
            pagingDataFlow = pagingDataFlow,
            lazyListState = lazyListState,
            showMoreInfo = showMoreInfoInReleaseListItem,
            eventSink = ::eventSink,
        )
    }
}

sealed interface ReleasesListUiEvent : CircuitUiEvent {
    data class Get(
        val browseMethod: BrowseMethod,
        val isRemote: Boolean = true,
    ) : ReleasesListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : ReleasesListUiEvent

    data class UpdateShowMoreInfoInReleaseListItem(
        val showMore: Boolean,
    ) : ReleasesListUiEvent

    data class RequestForMissingCoverArtUrl(
        val entityId: String,
    ) : ReleasesListUiEvent
}

@Stable
data class ReleasesListUiState(
    val pagingDataFlow: Flow<PagingData<ListItemModel>> = emptyFlow(),
    val lazyListState: LazyListState = LazyListState(),
    val showMoreInfo: Boolean = true,
    val eventSink: (ReleasesListUiEvent) -> Unit = {},
) : CircuitUiState
