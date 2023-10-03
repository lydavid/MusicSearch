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
import androidx.compose.ui.res.stringResource
import ly.david.musicsearch.domain.history.HistorySortOption
import ly.david.ui.common.R
import ly.david.ui.common.component.ClickableItem
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
            onSortOptionClick = onSortOptionClick
        )
    }
}

@Composable
private fun HistorySortBottomSheetContent(
    sortOption: HistorySortOption = HistorySortOption.RECENTLY_VISITED,
    onSortOptionClick: (HistorySortOption) -> Unit = {},
) {
    Column {
        HistorySortOption.entries.forEach {
            ClickableItem(
                title = stringResource(id = it.labelRes),
                endIcon = if (sortOption == it) Icons.Default.Check else null,
                onClick = {
                    onSortOptionClick(it)
                }
            )
        }
    }
}

internal val HistorySortOption.labelRes: Int
    get() {
        return when (this) {
            HistorySortOption.ALPHABETICALLY -> R.string.alphabetically
            HistorySortOption.ALPHABETICALLY_REVERSE -> R.string.alphabetically_reverse
            HistorySortOption.LEAST_RECENTLY_VISITED -> R.string.least_recently_visited
            HistorySortOption.RECENTLY_VISITED -> R.string.recently_visited
            HistorySortOption.MOST_VISITED -> R.string.most_visited
            HistorySortOption.LEAST_VISITED -> R.string.least_visited
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
