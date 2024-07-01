package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            DotsFlashing()
        }
    }
}
