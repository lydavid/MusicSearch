package ly.david.musicsearch.ui.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun LargeImage(
    url: String,
    mbid: String,
    modifier: Modifier = Modifier,
)
