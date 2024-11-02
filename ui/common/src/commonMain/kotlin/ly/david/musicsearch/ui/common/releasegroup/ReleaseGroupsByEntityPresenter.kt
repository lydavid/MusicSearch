package ly.david.musicsearch.ui.common.releasegroup

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupImageRepository
import ly.david.musicsearch.shared.domain.releasegroup.usecase.GetReleaseGroupsByEntity

class ReleaseGroupsByEntityPresenter(
    private val getReleaseGroupsByEntity: GetReleaseGroupsByEntity,
    private val appPreferences: AppPreferences,
    private val releaseGroupImageRepository: ReleaseGroupImageRepository,
) : Presenter<ReleaseGroupsByEntityUiState> {
    @Composable
    override fun present(): ReleaseGroupsByEntityUiState {
        val scope = rememberCoroutineScope()
        val sorted by appPreferences.sortReleaseGroupListItems.collectAsState(true)
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var releaseGroupListItems: Flow<PagingData<ListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val lazyListState: LazyListState = rememberLazyListState()

        LaunchedEffect(
            id,
            entity,
            query,
            sorted,
        ) {
            if (id.isEmpty()) return@LaunchedEffect
            val capturedEntity = entity ?: return@LaunchedEffect

            releaseGroupListItems = getReleaseGroupsByEntity(
                entityId = id,
                entity = capturedEntity,
                listFilters = ListFilters(
                    query = query,
                    sorted = sorted,
                    isRemote = isRemote,
                ),
            )
        }

        fun eventSink(event: ReleaseGroupsByEntityUiEvent) {
            when (event) {
                is ReleaseGroupsByEntityUiEvent.RequestForMissingCoverArtUrl -> {
                    scope.launch {
                        releaseGroupImageRepository.getReleaseGroupImageUrl(
                            releaseGroupId = event.entityId,
                            thumbnail = true,
                            forceRefresh = false,
                        )
                    }
                }

                is ReleaseGroupsByEntityUiEvent.UpdateSortReleaseGroupListItem -> {
                    appPreferences.setSortReleaseGroupListItems(event.sort)
                }

                is ReleaseGroupsByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is ReleaseGroupsByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return ReleaseGroupsByEntityUiState(
            lazyPagingItems = releaseGroupListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            sort = sorted,
            eventSink = ::eventSink,
        )
    }
}
