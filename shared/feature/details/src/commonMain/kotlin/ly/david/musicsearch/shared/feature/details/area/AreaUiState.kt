package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.area.AreaScaffoldModel
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel

@Stable
internal data class AreaUiState(
    val title: String,
    val isError: Boolean,
    val area: AreaScaffoldModel?,
    val tabs: List<AreaTab>,
    val selectedTab: AreaTab,
    val query: String,
    val placesLazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val eventSink: (AreaUiEvent) -> Unit,
) : CircuitUiState
