package ly.david.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import ly.david.musicsearch.core.models.history.HistorySortOption
import ly.david.ui.common.component.ClickableItem
import ly.david.musicsearch.strings.AppStrings
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

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
private fun HistorySortBottomSheetContent(
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

@DefaultPreviews
@Composable
internal fun PreviewHistorySortBottomSheetContent() {
    PreviewTheme {
        Surface {
            HistorySortBottomSheetContent()
        }
    }
}
