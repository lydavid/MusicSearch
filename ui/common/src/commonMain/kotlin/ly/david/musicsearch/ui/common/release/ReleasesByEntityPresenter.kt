package ly.david.musicsearch.ui.common.release

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
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository
import ly.david.musicsearch.shared.domain.release.usecase.GetReleasesByEntity

class ReleasesByEntityPresenter(
    private val getReleasesByEntity: GetReleasesByEntity,
    private val appPreferences: AppPreferences,
    private val releaseImageRepository: ReleaseImageRepository,
) : Presenter<ReleasesByEntityUiState> {
    @Composable
    override fun present(): ReleasesByEntityUiState {
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val releaseListItems: Flow<PagingData<ReleaseListItemModel>> by rememberRetained(id, entity, query) {
            mutableStateOf(
                getReleasesByEntity(
                    entityId = id,
                    entity = entity,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                    ),
                ),
            )
        }
        val lazyListState: LazyListState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val showMoreInfoInReleaseListItem by appPreferences.showMoreInfoInReleaseListItem.collectAsRetainedState(true)

        fun eventSink(event: ReleasesByEntityUiEvent) {
            when (event) {
                is ReleasesByEntityUiEvent.RequestForMissingCoverArtUrl -> {
                    scope.launch {
                        releaseImageRepository.getImageMetadata(
                            mbid = event.entityId,
                            forceRefresh = false,
                        )
                    }
                }

                is ReleasesByEntityUiEvent.UpdateShowMoreInfoInReleaseListItem -> {
                    appPreferences.setShowMoreInfoInReleaseListItem(event.showMore)
                }

                is ReleasesByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is ReleasesByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return ReleasesByEntityUiState(
            lazyPagingItems = releaseListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            showMoreInfo = showMoreInfoInReleaseListItem,
            eventSink = ::eventSink,
        )
    }
}

sealed interface ReleasesByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : ReleasesByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : ReleasesByEntityUiEvent

    data class UpdateShowMoreInfoInReleaseListItem(
        val showMore: Boolean,
    ) : ReleasesByEntityUiEvent

    data class RequestForMissingCoverArtUrl(
        val entityId: String,
    ) : ReleasesByEntityUiEvent
}

@Stable
data class ReleasesByEntityUiState(
    val lazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val showMoreInfo: Boolean = true,
    val eventSink: (ReleasesByEntityUiEvent) -> Unit = {},
) : CircuitUiState
