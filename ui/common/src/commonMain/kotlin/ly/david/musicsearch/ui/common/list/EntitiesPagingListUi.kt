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
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun EntitiesPagingListUi(
    uiState: EntitiesPagingListUiState,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    now: Instant = Clock.System.now(),
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    selectedIds: ImmutableSet<String> = persistentSetOf(),
    onSelect: (String) -> Unit = {},
    requestForMissingCoverArtUrl: (entityId: String) -> Unit = {},
) {
    EntitiesListScreen(
        uiState = uiState,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        now = now,
        onItemClick = onItemClick,
        selectedIds = selectedIds,
        onSelect = onSelect,
        requestForMissingCoverArtUrl = { id ->
            requestForMissingCoverArtUrl(
                id,
            )
        },
    )
}
