package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.ActionableResult
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.ui.common.artist.ArtistsByEntityUiState
import ly.david.ui.common.event.EventsByEntityUiState
import ly.david.ui.common.release.ReleasesByEntityUiState
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityUiState

@Stable
internal data class CollectionUiState(
    val collection: CollectionListItemModel?,
    val actionableResult: ActionableResult?,
    val query: String,
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val artistsByEntityUiState: ArtistsByEntityUiState,
    val eventsByEntityUiState: EventsByEntityUiState,
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val releaseGroupsByEntityUiState: ReleaseGroupsByEntityUiState,
    val eventSink: (CollectionUiEvent) -> Unit,
) : CircuitUiState
