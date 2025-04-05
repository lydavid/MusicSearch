package ly.david.musicsearch.ui.common.genre

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
import ly.david.musicsearch.shared.domain.genre.usecase.GetGenres
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class GenresListPresenter(
    private val getGenres: GetGenres,
) : Presenter<GenresListUiState> {
    @Composable
    override fun present(): GenresListUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        val genreListItems: Flow<PagingData<GenreListItemModel>> by rememberRetained(query, id, entity) {
            mutableStateOf(
                getGenres(
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

        fun eventSink(event: GenresListUiEvent) {
            when (event) {
                is GenresListUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is GenresListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return GenresListUiState(
            lazyPagingItems = genreListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface GenresListUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity?,
        val isRemote: Boolean = true,
    ) : GenresListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : GenresListUiEvent
}

@Stable
data class GenresListUiState(
    val lazyPagingItems: LazyPagingItems<GenreListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (GenresListUiEvent) -> Unit = {},
) : CircuitUiState
