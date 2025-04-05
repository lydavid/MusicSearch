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
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.usecase.GetArtists
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver

class ArtistsListPresenter(
    private val getArtists: GetArtists,
) : Presenter<ArtistsListUiState> {
    @Composable
    override fun present(): ArtistsListUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val artistListItems: Flow<PagingData<ArtistListItemModel>> by rememberRetained(query, browseMethod) {
            mutableStateOf(
                getArtists(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                    ),
                ),
            )
        }
        val lazyListState: LazyListState = rememberLazyListState()

        fun eventSink(event: ArtistsListUiEvent) {
            when (event) {
                is ArtistsListUiEvent.Get -> {
                    browseMethod = event.browseMethod
                    isRemote = event.isRemote
                }

                is ArtistsListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return ArtistsListUiState(
            lazyPagingItems = artistListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface ArtistsListUiEvent : CircuitUiEvent {

    data class Get(
        val browseMethod: BrowseMethod,
        val isRemote: Boolean = true,
    ) : ArtistsListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : ArtistsListUiEvent
}

@Stable
data class ArtistsListUiState(
    val lazyPagingItems: LazyPagingItems<ArtistListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val showMoreInfo: Boolean = true,
    val eventSink: (ArtistsListUiEvent) -> Unit = {},
) : CircuitUiState
