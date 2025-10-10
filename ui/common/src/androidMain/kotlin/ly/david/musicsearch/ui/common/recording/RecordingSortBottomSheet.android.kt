package ly.david.musicsearch.ui.common.recording

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.recording.RecordingSortOption
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewRecordingSortBottomSheetContentNone() {
    PreviewTheme {
        Surface {
            RecordingSortBottomSheetContent(
                sortOption = RecordingSortOption.None,
                onSortOptionClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewRecordingSortBottomSheetContentByDateAscending() {
    PreviewTheme {
        Surface {
            RecordingSortBottomSheetContent(
                sortOption = RecordingSortOption.DateAscending,
                onSortOptionClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewRecordingSortBottomSheetContentByDateDescending() {
    PreviewTheme {
        Surface {
            RecordingSortBottomSheetContent(
                sortOption = RecordingSortOption.DateDescending,
                onSortOptionClick = {},
            )
        }
    }
}
