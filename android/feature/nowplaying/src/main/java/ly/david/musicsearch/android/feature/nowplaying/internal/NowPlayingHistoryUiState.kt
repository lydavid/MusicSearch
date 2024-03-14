package ly.david.musicsearch.android.feature.nowplaying.internal

import androidx.compose.runtime.Stable
import androidx.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.listitem.ListItemModel

@Stable
internal data class NowPlayingHistoryUiState(
    val query: String,
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val eventSink: (NowPlayingHistoryUiEvent) -> Unit,
) : CircuitUiState
