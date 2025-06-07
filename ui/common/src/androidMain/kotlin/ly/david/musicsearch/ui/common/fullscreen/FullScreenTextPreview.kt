package ly.david.musicsearch.ui.common.fullscreen

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewFullScreenText() {
    PreviewTheme {
        Surface {
            FullScreenText("This is text that spans the width the screen, let's see if it's padded")
        }
    }
}
