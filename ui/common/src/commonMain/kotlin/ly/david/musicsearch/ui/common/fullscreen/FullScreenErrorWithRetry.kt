package ly.david.musicsearch.ui.common.fullscreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.button.RetryButton

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
