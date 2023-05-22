package ly.david.ui.common.fullscreen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme

@Composable
fun FullScreenLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    FullScreenContent(modifier = modifier) {
        CircularProgressIndicator()
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            FullScreenLoadingIndicator()
        }
    }
}
