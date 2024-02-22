package ly.david.ui.history

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@DefaultPreviews
@Composable
internal fun PreviewHistorySortBottomSheetContent() {
    PreviewTheme {
        Surface {
            HistorySortBottomSheetContent()
        }
    }
}
