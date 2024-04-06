package ly.david.musicsearch.ui.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.ui.image.internal.PlaceholderIcon

@Composable
actual fun LargeImage(
    url: String,
    mbid: String,
    modifier: Modifier,
) {
    // TODO: handle ios large image
    PlaceholderIcon(
        modifier = modifier,
    )
}
