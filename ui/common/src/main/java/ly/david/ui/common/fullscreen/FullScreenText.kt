package ly.david.ui.common.fullscreen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
fun FullScreenText(
    text: String,
    modifier: Modifier = Modifier,
) {
    FullScreenContent(modifier = modifier) {
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            text = text
        )
    }
}

@DefaultPreviews
@Composable
internal fun PreviewFullScreenText() {
    PreviewTheme {
        Surface {
            FullScreenText("This is text that spans the width the screen, let's see if it's padded")
        }
    }
}
