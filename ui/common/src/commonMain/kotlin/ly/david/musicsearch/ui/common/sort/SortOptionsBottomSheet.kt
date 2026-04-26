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
import ly.david.musicsearch.shared.domain.sort.AreaSortOption
import ly.david.musicsearch.shared.domain.sort.ArtistSortOption
import ly.david.musicsearch.shared.domain.sort.EventSortOption
import ly.david.musicsearch.shared.domain.sort.LabelSortOption
import ly.david.musicsearch.shared.domain.sort.PlaceSortOption
import ly.david.musicsearch.shared.domain.sort.RecordingSortOption
import ly.david.musicsearch.shared.domain.sort.ReleaseGroupSortOption
import ly.david.musicsearch.shared.domain.sort.ReleaseSortOption
import ly.david.musicsearch.shared.domain.sort.SortableOption
import ly.david.musicsearch.shared.domain.sort.WorkSortOption
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.Check
import ly.david.musicsearch.ui.common.icons.CustomIcons
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.addressAlphabetically
import musicsearch.ui.common.generated.resources.addressReverseAlphabetically
import musicsearch.ui.common.generated.resources.alphabetically
import musicsearch.ui.common.generated.resources.alphabeticallyReverse
import musicsearch.ui.common.generated.resources.earliestBeginDate
import musicsearch.ui.common.generated.resources.earliestCached
import musicsearch.ui.common.generated.resources.earliestOpenDate
import musicsearch.ui.common.generated.resources.earliestReleaseDate
import musicsearch.ui.common.generated.resources.earliestStartDate
import musicsearch.ui.common.generated.resources.labelCodeAscending
import musicsearch.ui.common.generated.resources.labelCodeDescending
import musicsearch.ui.common.generated.resources.latestBeginDate
import musicsearch.ui.common.generated.resources.latestCached
import musicsearch.ui.common.generated.resources.latestOpenDate
import musicsearch.ui.common.generated.resources.latestReleaseDate
import musicsearch.ui.common.generated.resources.latestStartDate
import musicsearch.ui.common.generated.resources.leastCompleteListened
import musicsearch.ui.common.generated.resources.leastListened
import musicsearch.ui.common.generated.resources.mostCompleteListened
import musicsearch.ui.common.generated.resources.mostListened
import musicsearch.ui.common.generated.resources.typeAlphabetically
import musicsearch.ui.common.generated.resources.typeReverseAlphabetically
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

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
    Column(Modifier.verticalScroll(rememberScrollState())) {
        sortOptions.forEach { sortOption ->
            ClickableItem(
                title = sortOption.getLabel(),
                endIcon = if (selectedSortOption == sortOption) CustomIcons.Check else null,
                onClick = {
                    onSortOptionClick(sortOption)
                },
            )
        }
    }
}

@Composable
private fun SortableOption.getLabel(): String {
    return stringResource(
        when (this) {
            is AreaSortOption -> getLabelRes()
            is ArtistSortOption -> getLabelRes()
            is EventSortOption -> getLabelRes()
            is LabelSortOption -> getLabelRes()
            is PlaceSortOption -> getLabelRes()
            is RecordingSortOption -> getLabelRes()
            is ReleaseSortOption -> getLabelRes()
            is ReleaseGroupSortOption -> getLabelRes()
            is WorkSortOption -> getLabelRes()
        },
    )
}

private fun AreaSortOption.getLabelRes(): StringResource {
    return when (this) {
        AreaSortOption.InsertedAscending -> Res.string.earliestCached
        AreaSortOption.InsertedDescending -> Res.string.latestCached

        AreaSortOption.NameAscending -> Res.string.alphabetically
        AreaSortOption.NameDescending -> Res.string.alphabeticallyReverse

        AreaSortOption.DateAscending -> Res.string.earliestBeginDate
        AreaSortOption.DateDescending -> Res.string.latestBeginDate
    }
}

private fun ArtistSortOption.getLabelRes(): StringResource {
    return when (this) {
        ArtistSortOption.InsertedAscending -> Res.string.earliestCached
        ArtistSortOption.InsertedDescending -> Res.string.latestCached

        ArtistSortOption.NameAscending -> Res.string.alphabetically
        ArtistSortOption.NameDescending -> Res.string.alphabeticallyReverse

        ArtistSortOption.DateAscending -> Res.string.earliestBeginDate
        ArtistSortOption.DateDescending -> Res.string.latestBeginDate
    }
}

private fun EventSortOption.getLabelRes(): StringResource {
    return when (this) {
        EventSortOption.InsertedAscending -> Res.string.earliestCached
        EventSortOption.InsertedDescending -> Res.string.latestCached

        EventSortOption.NameAscending -> Res.string.alphabetically
        EventSortOption.NameDescending -> Res.string.alphabeticallyReverse

        EventSortOption.DateAscending -> Res.string.earliestStartDate
        EventSortOption.DateDescending -> Res.string.latestStartDate
    }
}

private fun LabelSortOption.getLabelRes(): StringResource {
    return when (this) {
        LabelSortOption.InsertedAscending -> Res.string.earliestCached
        LabelSortOption.InsertedDescending -> Res.string.latestCached

        LabelSortOption.NameAscending -> Res.string.alphabetically
        LabelSortOption.NameDescending -> Res.string.alphabeticallyReverse

        LabelSortOption.DateAscending -> Res.string.earliestBeginDate
        LabelSortOption.DateDescending -> Res.string.latestBeginDate

        LabelSortOption.CodeAscending -> Res.string.labelCodeAscending
        LabelSortOption.CodeDescending -> Res.string.labelCodeDescending
    }
}

private fun PlaceSortOption.getLabelRes(): StringResource {
    return when (this) {
        PlaceSortOption.InsertedAscending -> Res.string.earliestCached
        PlaceSortOption.InsertedDescending -> Res.string.latestCached

        PlaceSortOption.NameAscending -> Res.string.alphabetically
        PlaceSortOption.NameDescending -> Res.string.alphabeticallyReverse

        PlaceSortOption.AddressAscending -> Res.string.addressAlphabetically
        PlaceSortOption.AddressDescending -> Res.string.addressReverseAlphabetically

        PlaceSortOption.DateAscending -> Res.string.earliestOpenDate
        PlaceSortOption.DateDescending -> Res.string.latestOpenDate
    }
}

private fun RecordingSortOption.getLabelRes(): StringResource {
    return when (this) {
        RecordingSortOption.InsertedAscending -> Res.string.earliestCached
        RecordingSortOption.InsertedDescending -> Res.string.latestCached

        RecordingSortOption.NameAscending -> Res.string.alphabetically
        RecordingSortOption.NameDescending -> Res.string.alphabeticallyReverse

        RecordingSortOption.DateAscending -> Res.string.earliestReleaseDate
        RecordingSortOption.DateDescending -> Res.string.latestReleaseDate

        RecordingSortOption.ListensAscending -> Res.string.leastListened
        RecordingSortOption.ListensDescending -> Res.string.mostListened
    }
}

private fun ReleaseSortOption.getLabelRes(): StringResource {
    return when (this) {
        ReleaseSortOption.InsertedAscending -> Res.string.earliestCached
        ReleaseSortOption.InsertedDescending -> Res.string.latestCached

        ReleaseSortOption.NameAscending -> Res.string.alphabetically
        ReleaseSortOption.NameDescending -> Res.string.alphabeticallyReverse

        ReleaseSortOption.DateAscending -> Res.string.earliestReleaseDate
        ReleaseSortOption.DateDescending -> Res.string.latestReleaseDate

        ReleaseSortOption.ListensAscending -> Res.string.leastListened
        ReleaseSortOption.ListensDescending -> Res.string.mostListened

        ReleaseSortOption.CompleteListensAscending -> Res.string.leastCompleteListened
        ReleaseSortOption.CompleteListensDescending -> Res.string.mostCompleteListened
    }
}

private fun ReleaseGroupSortOption.getLabelRes(): StringResource {
    return when (this) {
        ReleaseGroupSortOption.InsertedAscending -> Res.string.earliestCached
        ReleaseGroupSortOption.InsertedDescending -> Res.string.latestCached

        ReleaseGroupSortOption.NameAscending -> Res.string.alphabetically
        ReleaseGroupSortOption.NameDescending -> Res.string.alphabeticallyReverse

        ReleaseGroupSortOption.DateAscending -> Res.string.earliestReleaseDate
        ReleaseGroupSortOption.DateDescending -> Res.string.latestReleaseDate

        ReleaseGroupSortOption.PrimaryTypeAscending -> Res.string.typeAlphabetically
        ReleaseGroupSortOption.PrimaryTypeDescending -> Res.string.typeReverseAlphabetically
    }
}

private fun WorkSortOption.getLabelRes(): StringResource {
    return when (this) {
        WorkSortOption.InsertedAscending -> Res.string.earliestCached
        WorkSortOption.InsertedDescending -> Res.string.latestCached

        WorkSortOption.NameAscending -> Res.string.alphabetically
        WorkSortOption.NameDescending -> Res.string.alphabeticallyReverse

        WorkSortOption.ListensAscending -> Res.string.leastListened
        WorkSortOption.ListensDescending -> Res.string.mostListened
    }
}
