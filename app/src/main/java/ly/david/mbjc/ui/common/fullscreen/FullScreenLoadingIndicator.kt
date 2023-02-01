package ly.david.mbjc.ui.common.fullscreen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun FullScreenLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    FullScreenContent(modifier = modifier) {
        CircularProgressIndicator()
    }
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            FullScreenLoadingIndicator()
        }
    }
}
