package ly.david.mbjc.ui.common.fullscreen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun FullScreenText(
    text: String,
) {
    FullScreenContent {
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            text = text
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            FullScreenText("Hello world")
        }
    }
}
