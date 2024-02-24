package ly.david.musicsearch.shared.feature.history

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.feature.history.internal.HistorySortBottomSheetContent
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
