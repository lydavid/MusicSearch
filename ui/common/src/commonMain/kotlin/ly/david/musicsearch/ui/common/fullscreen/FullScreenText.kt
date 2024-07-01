package ly.david.musicsearch.ui.common.fullscreen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun FullScreenText(
    text: String,
    modifier: Modifier = Modifier,
) {
    FullScreenContent(modifier = modifier) {
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            text = text,
        )
    }
}
