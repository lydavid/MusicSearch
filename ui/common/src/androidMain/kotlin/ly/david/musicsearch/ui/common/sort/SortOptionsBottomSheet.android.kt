package ly.david.musicsearch.ui.common.sort

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.recording.RecordingSortOption
import ly.david.musicsearch.shared.domain.release.ReleaseSortOption
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewSortOptionsBottomSheetContentNone() {
    PreviewTheme {
        Surface {
            SortOptionsBottomSheetContent(
                sortOptions = RecordingSortOption.entries,
                selectedSortOption = RecordingSortOption.InsertedAscending,
                onSortOptionClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSortOptionsBottomSheetContentByDateAscending() {
    PreviewTheme {
        Surface {
            SortOptionsBottomSheetContent(
                sortOptions = RecordingSortOption.entries,
                selectedSortOption = RecordingSortOption.DateAscending,
                onSortOptionClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSortOptionsBottomSheetContentByDateDescending() {
    PreviewTheme {
        Surface {
            SortOptionsBottomSheetContent(
                sortOptions = RecordingSortOption.entries,
                selectedSortOption = RecordingSortOption.DateDescending,
                onSortOptionClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSortOptionsBottomSheetContentByCompleteListensAscending() {
    PreviewTheme {
        Surface {
            SortOptionsBottomSheetContent(
                sortOptions = ReleaseSortOption.entries,
                selectedSortOption = ReleaseSortOption.CompleteListensAscending,
                onSortOptionClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSortOptionsBottomSheetContentByCompleteListensDescending() {
    PreviewTheme {
        Surface {
            SortOptionsBottomSheetContent(
                sortOptions = ReleaseSortOption.entries,
                selectedSortOption = ReleaseSortOption.CompleteListensDescending,
                onSortOptionClick = {},
            )
        }
    }
}
