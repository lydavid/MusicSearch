package ly.david.musicsearch.ui.common.genre

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.genre.usecase.GetGenresByEntity
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class GenresByEntityPresenter(
    private val getGenresByEntity: GetGenresByEntity,
) : Presenter<GenresByEntityUiState> {
    @Composable
    override fun present(): GenresByEntityUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var genreListItems: Flow<PagingData<GenreListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val lazyListState: LazyListState = rememberLazyListState()

        LaunchedEffect(
            key1 = id,
            key2 = entity,
            key3 = query,
        ) {
            if (id.isEmpty()) return@LaunchedEffect
            val capturedEntity = entity ?: return@LaunchedEffect

            genreListItems = getGenresByEntity(
                entityId = id,
                entity = capturedEntity,
                listFilters = ListFilters(
                    query = query,
                    isRemote = isRemote,
                ),
            )
        }

        fun eventSink(event: GenresByEntityUiEvent) {
            when (event) {
                is GenresByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is GenresByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return GenresByEntityUiState(
            lazyPagingItems = genreListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface GenresByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : GenresByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : GenresByEntityUiEvent
}

@Stable
data class GenresByEntityUiState(
    val lazyPagingItems: LazyPagingItems<GenreListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (GenresByEntityUiEvent) -> Unit = {},
) : CircuitUiState
