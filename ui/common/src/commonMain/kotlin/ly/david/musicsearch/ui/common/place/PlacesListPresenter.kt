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
import ly.david.musicsearch.shared.domain.place.usecase.GetPlaces

class PlacesListPresenter(
    private val getPlaces: GetPlaces,
) : Presenter<PlacesListUiState> {
    @Composable
    override fun present(): PlacesListUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val placeListItems: Flow<PagingData<PlaceListItemModel>> by rememberRetained(query, id, entity) {
            mutableStateOf(
                getPlaces(
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

        fun eventSink(event: PlacesListUiEvent) {
            when (event) {
                is PlacesListUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is PlacesListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return PlacesListUiState(
            lazyPagingItems = placeListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface PlacesListUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : PlacesListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : PlacesListUiEvent
}

@Stable
data class PlacesListUiState(
    val lazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (PlacesListUiEvent) -> Unit = {},
) : CircuitUiState
