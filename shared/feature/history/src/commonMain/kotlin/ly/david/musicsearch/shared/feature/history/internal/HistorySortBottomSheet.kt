package ly.david.musicsearch.shared.feature.history.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.history.HistorySortOption
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.common.component.ClickableItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistorySortBottomSheet(
    sortOption: HistorySortOption,
    onSortOptionClick: (HistorySortOption) -> Unit = {},
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
    ) {
        HistorySortBottomSheetContent(
            sortOption = sortOption,
            onSortOptionClick = onSortOptionClick,
        )
    }
}

@Composable
internal fun HistorySortBottomSheetContent(
    sortOption: HistorySortOption = HistorySortOption.RECENTLY_VISITED,
    onSortOptionClick: (HistorySortOption) -> Unit = {},
) {
    val strings = LocalStrings.current

    Column {
        HistorySortOption.entries.forEach {
            ClickableItem(
                title = it.getLabel(strings),
                endIcon = if (sortOption == it) Icons.Default.Check else null,
                onClick = {
                    onSortOptionClick(it)
                },
            )
        }
    }
}

private fun HistorySortOption.getLabel(strings: AppStrings): String {
    return when (this) {
        HistorySortOption.ALPHABETICALLY -> strings.alphabetically
        HistorySortOption.ALPHABETICALLY_REVERSE -> strings.alphabeticallyReverse
        HistorySortOption.LEAST_RECENTLY_VISITED -> strings.leastRecentlyVisited
        HistorySortOption.RECENTLY_VISITED -> strings.recentlyVisited
        HistorySortOption.MOST_VISITED -> strings.mostVisited
        HistorySortOption.LEAST_VISITED -> strings.leastVisited
    }
}
