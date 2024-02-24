package ly.david.musicsearch.shared.feature.search

import androidx.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.parcelize.CommonParcelize

@CommonParcelize
data class SearchScreen(
    val query: String? = null,
    val entity: MusicBrainzEntity? = null,
) : Screen {
    data class UiState(
        val query: String,
        val entity: MusicBrainzEntity,
        val searchResults: LazyPagingItems<ListItemModel>,
        val searchHistory: LazyPagingItems<ListItemModel>,
        val eventSink: (UiEvent) -> Unit,
    ) : CircuitUiState

    sealed interface UiEvent : CircuitUiEvent {
        data class UpdateEntity(val entity: MusicBrainzEntity) : UiEvent
        data class UpdateQuery(val query: String) : UiEvent
        data object RecordSearch : UiEvent
        data class DeleteSearchHistory(val item: SearchHistoryListItemModel) : UiEvent
        data object DeleteAllEntitySearchHistory : UiEvent
        data class ClickItem(val entity: MusicBrainzEntity, val id: String, val title: String?) : UiEvent
    }
}
