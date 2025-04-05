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
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.release.usecase.GetReleases

class ReleasesListPresenter(
    private val getReleases: GetReleases,
    private val appPreferences: AppPreferences,
    private val imageMetadataRepository: ImageMetadataRepository,
) : Presenter<ReleasesListUiState> {
    @Composable
    override fun present(): ReleasesListUiState {
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val releaseListItems: Flow<PagingData<ReleaseListItemModel>> by rememberRetained(id, entity, query) {
            mutableStateOf(
                getReleases(
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

        fun eventSink(event: ReleasesListUiEvent) {
            when (event) {
                is ReleasesListUiEvent.RequestForMissingCoverArtUrl -> {
                    scope.launch {
                        imageMetadataRepository.getImageMetadata(
                            mbid = event.entityId,
                            entity = MusicBrainzEntity.RELEASE,
                            forceRefresh = false,
                        )
                    }
                }

                is ReleasesListUiEvent.UpdateShowMoreInfoInReleaseListItem -> {
                    appPreferences.setShowMoreInfoInReleaseListItem(event.showMore)
                }

                is ReleasesListUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is ReleasesListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return ReleasesListUiState(
            lazyPagingItems = releaseListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            showMoreInfo = showMoreInfoInReleaseListItem,
            eventSink = ::eventSink,
        )
    }
}

sealed interface ReleasesListUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity?,
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
    val lazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val showMoreInfo: Boolean = true,
    val eventSink: (ReleasesListUiEvent) -> Unit = {},
) : CircuitUiState
