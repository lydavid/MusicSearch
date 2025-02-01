package ly.david.musicsearch.ui.common.series

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
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.series.usecase.GetSeriesByEntity

class SeriesByEntityPresenter(
    private val getSeriesByEntity: GetSeriesByEntity,
) : Presenter<SeriesByEntityUiState> {
    @Composable
    override fun present(): SeriesByEntityUiState {
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val seriesListItems: Flow<PagingData<SeriesListItemModel>> by rememberRetained(id, entity, query) {
            mutableStateOf(
                getSeriesByEntity(
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

        fun eventSink(event: SeriesByEntityUiEvent) {
            when (event) {
                is SeriesByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is SeriesByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return SeriesByEntityUiState(
            lazyPagingItems = seriesListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface SeriesByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : SeriesByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : SeriesByEntityUiEvent
}

@Stable
data class SeriesByEntityUiState(
    val lazyPagingItems: LazyPagingItems<SeriesListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (SeriesByEntityUiEvent) -> Unit = {},
) : CircuitUiState
