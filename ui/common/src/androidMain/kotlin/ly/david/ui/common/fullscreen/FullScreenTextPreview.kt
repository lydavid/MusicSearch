package ly.david.ui.common.fullscreen

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@DefaultPreviews
@Composable
internal fun PreviewFullScreenText() {
    PreviewTheme {
        Surface {
            FullScreenText("This is text that spans the width the screen, let's see if it's padded")
        }
    }
}
