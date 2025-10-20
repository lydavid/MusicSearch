package ly.david.musicsearch.ui.common.sort

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.list.SortableOption
import ly.david.musicsearch.shared.domain.recording.RecordingSortOption
import ly.david.musicsearch.shared.domain.release.ReleaseSortOption
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupSortOption
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.Check
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.theme.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T> SortOptionsBottomSheet(
    sortOptions: List<T>,
    selectedSortOption: T,
    onSortOptionClick: (T) -> Unit,
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit = {},
) where T : Enum<T>, T : SortableOption {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
    ) {
        SortOptionsBottomSheetContent(
            sortOptions = sortOptions,
            selectedSortOption = selectedSortOption,
            onSortOptionClick = onSortOptionClick,
        )
    }
}

@Composable
internal fun <T> SortOptionsBottomSheetContent(
    sortOptions: List<T>,
    selectedSortOption: T,
    onSortOptionClick: (T) -> Unit,
) where T : Enum<T>, T : SortableOption {
    val strings = LocalStrings.current

    Column(Modifier.verticalScroll(rememberScrollState())) {
        sortOptions.forEach { sortOption ->
            ClickableItem(
                title = sortOption.getLabel(strings),
                endIcon = if (selectedSortOption == sortOption) CustomIcons.Check else null,
                onClick = {
                    onSortOptionClick(sortOption)
                },
            )
        }
    }
}

private fun SortableOption.getLabel(strings: AppStrings): String {
    return when (this) {
        is RecordingSortOption -> getLabel(strings)
        is ReleaseSortOption -> getLabel(strings)
        is ReleaseGroupSortOption -> getLabel(strings)
        else -> error("Unsupported SortOption type: ${this::class.simpleName}")
    }
}

private fun RecordingSortOption.getLabel(strings: AppStrings): String {
    return when (this) {
        RecordingSortOption.InsertedAscending -> strings.earliestCached
        RecordingSortOption.InsertedDescending -> strings.latestCached

        RecordingSortOption.NameAscending -> strings.alphabetically
        RecordingSortOption.NameDescending -> strings.alphabeticallyReverse

        RecordingSortOption.DateAscending -> strings.earliestReleaseDate
        RecordingSortOption.DateDescending -> strings.latestReleaseDate

        RecordingSortOption.ListensAscending -> strings.leastListened
        RecordingSortOption.ListensDescending -> strings.mostListened
    }
}

private fun ReleaseSortOption.getLabel(strings: AppStrings): String {
    return when (this) {
        ReleaseSortOption.InsertedAscending -> strings.earliestCached
        ReleaseSortOption.InsertedDescending -> strings.latestCached

        ReleaseSortOption.NameAscending -> strings.alphabetically
        ReleaseSortOption.NameDescending -> strings.alphabeticallyReverse

        ReleaseSortOption.DateAscending -> strings.earliestReleaseDate
        ReleaseSortOption.DateDescending -> strings.latestReleaseDate

        ReleaseSortOption.ListensAscending -> strings.leastListened
        ReleaseSortOption.ListensDescending -> strings.mostListened

        ReleaseSortOption.CompleteListensAscending -> strings.leastCompleteListened
        ReleaseSortOption.CompleteListensDescending -> strings.mostCompleteListened
    }
}

private fun ReleaseGroupSortOption.getLabel(strings: AppStrings): String {
    return when (this) {
        ReleaseGroupSortOption.InsertedAscending -> strings.earliestCached
        ReleaseGroupSortOption.InsertedDescending -> strings.latestCached

        ReleaseGroupSortOption.NameAscending -> strings.alphabetically
        ReleaseGroupSortOption.NameDescending -> strings.alphabeticallyReverse

        ReleaseGroupSortOption.DateAscending -> strings.earliestReleaseDate
        ReleaseGroupSortOption.DateDescending -> strings.latestReleaseDate

        ReleaseGroupSortOption.PrimaryTypeAscending -> strings.typeAlphabetically
        ReleaseGroupSortOption.PrimaryTypeDescending -> strings.typeReverseAlphabetically
    }
}
