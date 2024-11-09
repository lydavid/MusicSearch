package ly.david.musicsearch.ui.common.artist

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
import ly.david.musicsearch.shared.domain.artist.usecase.GetArtistsByEntity
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class ArtistsByEntityPresenter(
    private val getArtistsByEntity: GetArtistsByEntity,
) : Presenter<ArtistsByEntityUiState> {
    @Composable
    override fun present(): ArtistsByEntityUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        val artistListItems: Flow<PagingData<ArtistListItemModel>> by rememberRetained(query, id, entity) {
            mutableStateOf(
                getArtistsByEntity(
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

        fun eventSink(event: ArtistsByEntityUiEvent) {
            when (event) {
                is ArtistsByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is ArtistsByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return ArtistsByEntityUiState(
            lazyPagingItems = artistListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface ArtistsByEntityUiEvent : CircuitUiEvent {

    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : ArtistsByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : ArtistsByEntityUiEvent
}

@Stable
data class ArtistsByEntityUiState(
    val lazyPagingItems: LazyPagingItems<ArtistListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val showMoreInfo: Boolean = true,
    val eventSink: (ArtistsByEntityUiEvent) -> Unit = {},
) : CircuitUiState
