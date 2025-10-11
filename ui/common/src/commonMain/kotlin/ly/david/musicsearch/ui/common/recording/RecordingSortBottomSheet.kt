package ly.david.musicsearch.ui.common.recording

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.recording.RecordingSortOption
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.Check
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.theme.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordingSortBottomSheet(
    sortOption: RecordingSortOption,
    onSortOptionClick: (RecordingSortOption) -> Unit,
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
    ) {
        RecordingSortBottomSheetContent(
            sortOption = sortOption,
            onSortOptionClick = onSortOptionClick,
        )
    }
}

@Composable
internal fun RecordingSortBottomSheetContent(
    sortOption: RecordingSortOption,
    onSortOptionClick: (RecordingSortOption) -> Unit = {},
) {
    val strings = LocalStrings.current

    Column(Modifier.verticalScroll(rememberScrollState())) {
        RecordingSortOption.entries.forEach {
            ClickableItem(
                title = it.getLabel(strings),
                endIcon = if (sortOption == it) CustomIcons.Check else null,
                onClick = {
                    onSortOptionClick(it)
                },
            )
        }
    }
}

internal fun RecordingSortOption.getLabel(strings: AppStrings): String {
    return when (this) {
        RecordingSortOption.None -> strings.none

        RecordingSortOption.NameAscending -> strings.alphabetically
        RecordingSortOption.NameDescending -> strings.alphabeticallyReverse

        RecordingSortOption.DateAscending -> strings.earliestReleaseDate
        RecordingSortOption.DateDescending -> strings.latestReleaseDate

        RecordingSortOption.ListensAscending -> strings.leastListened
        RecordingSortOption.ListensDescending -> strings.mostListened
    }
}
