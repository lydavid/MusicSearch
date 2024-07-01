package ly.david.musicsearch.ui.common.place

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.place.usecase.GetPlacesByEntity

class PlacesByEntityPresenter(
    private val getPlacesByEntity: GetPlacesByEntity,
) : Presenter<PlacesByEntityUiState> {
    @Composable
    override fun present(): PlacesByEntityUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var placeListItems: Flow<PagingData<PlaceListItemModel>> by remember { mutableStateOf(emptyFlow()) }

        LaunchedEffect(
            key1 = id,
            key2 = entity,
            key3 = query,
        ) {
            if (id.isEmpty()) return@LaunchedEffect
            val capturedEntity = entity ?: return@LaunchedEffect

            placeListItems = getPlacesByEntity(
                entityId = id,
                entity = capturedEntity,
                listFilters = ListFilters(
                    query = query,
                ),
            )
        }

        fun eventSink(event: PlacesByEntityUiEvent) {
            when (event) {
                is PlacesByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                }

                is PlacesByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return PlacesByEntityUiState(
            lazyPagingItems = placeListItems.collectAsLazyPagingItems(),
            eventSink = ::eventSink,
        )
    }
}
