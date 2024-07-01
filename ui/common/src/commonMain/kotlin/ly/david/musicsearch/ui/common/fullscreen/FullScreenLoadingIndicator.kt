package ly.david.musicsearch.ui.common.fullscreen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FullScreenLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    FullScreenContent(modifier = modifier) {
        CircularProgressIndicator()
    }
}
