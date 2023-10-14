package ly.david.ui.common.fullscreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ly.david.ui.common.button.RetryButton
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
fun FullScreenErrorWithRetry(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    FullScreenContent(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            text = "Couldn't fetch data from Music Brainz.\nCome back later or click below to try again.",
        )
        RetryButton(onClick = onClick)
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            FullScreenErrorWithRetry()
        }
    }
}
