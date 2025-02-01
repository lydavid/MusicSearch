package ly.david.musicsearch.ui.common.place

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.place.usecase.GetPlacesByEntity

class PlacesByEntityPresenter(
    private val getPlacesByEntity: GetPlacesByEntity,
) : Presenter<PlacesByEntityUiState> {
    @Composable
    override fun present(): PlacesByEntityUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val placeListItems: Flow<PagingData<PlaceListItemModel>> by rememberRetained(query, id, entity) {
            mutableStateOf(
                getPlacesByEntity(
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

        fun eventSink(event: PlacesByEntityUiEvent) {
            when (event) {
                is PlacesByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is PlacesByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return PlacesByEntityUiState(
            lazyPagingItems = placeListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface PlacesByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : PlacesByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : PlacesByEntityUiEvent
}

@Stable
data class PlacesByEntityUiState(
    val lazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (PlacesByEntityUiEvent) -> Unit = {},
) : CircuitUiState
