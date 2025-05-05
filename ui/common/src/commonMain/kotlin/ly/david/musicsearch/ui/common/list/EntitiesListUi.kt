package ly.david.musicsearch.ui.common.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun EntitiesListUi(
    uiState: EntitiesListUiState,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    selectedIds: ImmutableSet<String> = persistentSetOf(),
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onSelect: (String) -> Unit = {},
    requestForMissingCoverArtUrl: (entityId: String) -> Unit = {},
) {
    EntitiesListScreen(
        uiState = uiState,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        selectedIds = selectedIds,
        onItemClick = onItemClick,
        onSelect = onSelect,
        requestForMissingCoverArtUrl = { id ->
            requestForMissingCoverArtUrl(
                id,
            )
        },
    )
}
