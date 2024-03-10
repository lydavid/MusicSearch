package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Stable
import app.cash.paging.compose.LazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.area.AreaScaffoldModel
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel

@Stable
internal data class AreaUiState(
    val title: String,
    val isError: Boolean,
    val area: AreaScaffoldModel?,
    val tabs: List<AreaTab>,
    val selectedTab: AreaTab,
    val query: String,
    val showMoreInfoInReleaseListItem: Boolean,
    val placesLazyPagingItems: LazyPagingItems<PlaceListItemModel>,
    val releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    val releasesLazyListState: LazyListState,
    val eventSink: (AreaUiEvent) -> Unit,
) : CircuitUiState
