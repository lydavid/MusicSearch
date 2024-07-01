package ly.david.ui.common.paging

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@DefaultPreviews
@Composable
private fun FooterLoadingIndicatorPreview() {
    PreviewTheme {
        Surface {
            FooterLoadingIndicator()
        }
    }
}
