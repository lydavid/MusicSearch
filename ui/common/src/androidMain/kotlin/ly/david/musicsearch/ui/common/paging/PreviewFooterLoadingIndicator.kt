package ly.david.musicsearch.ui.common.paging

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewFooterLoadingIndicator() {
    PreviewTheme {
        Surface {
            FooterLoadingIndicator()
        }
    }
}
