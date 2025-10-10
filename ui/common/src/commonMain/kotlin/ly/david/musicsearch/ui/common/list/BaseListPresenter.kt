package ly.david.musicsearch.ui.common.list

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
import ly.david.musicsearch.shared.domain.list.GetEntities
import ly.david.musicsearch.shared.domain.list.ObserveLocalCount
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.recording.RecordingSortOption
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver

abstract class BaseListPresenter(
    private val getEntities: GetEntities,
    private val observeLocalCount: ObserveLocalCount,
    private val appPreferences: AppPreferences,
    private val musicBrainzImageMetadataRepository: MusicBrainzImageMetadataRepository,
) : Presenter<EntitiesListUiState> {

    abstract fun getEntityType(): MusicBrainzEntityType

    @Composable
    override fun present(): EntitiesListUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val recordingSortOption by appPreferences.recordingSortOption.collectAsRetainedState(RecordingSortOption.None)
        val sortReleaseGroups by appPreferences.sortReleaseGroupListItems.collectAsRetainedState(false)
        val sortReleases by appPreferences.sortReleaseListItems.collectAsRetainedState(false)
        val sort = when (getEntityType()) {
            MusicBrainzEntityType.RELEASE -> sortReleases
            MusicBrainzEntityType.RELEASE_GROUP -> sortReleaseGroups
            else -> false
        }
        val pagingDataFlow: Flow<PagingData<ListItemModel>> by rememberRetained(
            query,
            browseMethod,
            sort,
            recordingSortOption,
        ) {
            mutableStateOf(
                getEntities(
                    entity = getEntityType(),
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                        sorted = sort,
                        recordingSortOption = recordingSortOption,
                    ),
                ),
            )
        }
        val count by observeLocalCount(
            browseEntity = getEntityType(),
            browseMethod = browseMethod,
        ).collectAsRetainedState(0)
        val lazyListState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val showMoreInfoInReleaseListItem by appPreferences.showMoreInfoInReleaseListItem.collectAsRetainedState(true)
        var requestedImageMetadataForIds: Set<String> by remember { mutableStateOf(setOf()) }

        return EntitiesListUiState(
            pagingDataFlow = pagingDataFlow,
            count = count,
            lazyListState = lazyListState,
            recordingSortOption = recordingSortOption,
            showMoreInfo = showMoreInfoInReleaseListItem,
            sort = sort,
            eventSink = { event ->
                when (event) {
                    is EntitiesListUiEvent.Get -> {
                        browseMethod = event.browseMethod
                        isRemote = event.isRemote
                    }

                    is EntitiesListUiEvent.UpdateQuery -> {
                        query = event.query
                    }

                    is EntitiesListUiEvent.RequestForMissingCoverArtUrl -> {
                        val entityId = event.entityId
                        if (!requestedImageMetadataForIds.contains(entityId)) {
                            requestedImageMetadataForIds = requestedImageMetadataForIds + setOf(entityId)
                            scope.launch {
                                musicBrainzImageMetadataRepository.saveImageMetadata(
                                    mbid = entityId,
                                    entity = getEntityType(),
                                    itemsCount = count,
                                )
                            }
                        }
                    }

                    is EntitiesListUiEvent.UpdateSortRecordingListItem -> {
                        appPreferences.setRecordingSortOption(event.sortOption)
                    }

                    is EntitiesListUiEvent.UpdateSortReleaseListItem -> {
                        appPreferences.setSortReleaseListItems(event.sort)
                    }

                    is EntitiesListUiEvent.UpdateShowMoreInfoInReleaseListItem -> {
                        appPreferences.setShowMoreInfoInReleaseListItem(event.showMore)
                    }

                    is EntitiesListUiEvent.UpdateSortReleaseGroupListItem -> {
                        appPreferences.setSortReleaseGroupListItems(event.sort)
                    }
                }
            },
        )
    }
}

sealed interface EntitiesListUiEvent : CircuitUiEvent {
    data class Get(
        val browseMethod: BrowseMethod,
        val isRemote: Boolean = true,
    ) : EntitiesListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : EntitiesListUiEvent

    data class RequestForMissingCoverArtUrl(
        val entityId: String,
    ) : EntitiesListUiEvent

    data class UpdateSortRecordingListItem(
        val sortOption: RecordingSortOption,
    ) : EntitiesListUiEvent

    data class UpdateSortReleaseListItem(
        val sort: Boolean,
    ) : EntitiesListUiEvent

    data class UpdateShowMoreInfoInReleaseListItem(
        val showMore: Boolean,
    ) : EntitiesListUiEvent

    data class UpdateSortReleaseGroupListItem(
        val sort: Boolean,
    ) : EntitiesListUiEvent
}

@Stable
data class EntitiesListUiState(
    val pagingDataFlow: Flow<PagingData<ListItemModel>> = emptyFlow(),
    val count: Int = 0,
    val lazyListState: LazyListState = LazyListState(),
    val showMoreInfo: Boolean = true,
    val sort: Boolean = true,
    val recordingSortOption: RecordingSortOption = RecordingSortOption.None,
    val eventSink: (EntitiesListUiEvent) -> Unit = {},
) : CircuitUiState
