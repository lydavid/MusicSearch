package ly.david.musicsearch.ui.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import ly.david.musicsearch.ui.image.internal.PlaceholderIcon

@Composable
actual fun ThumbnailImage(
    url: String,
    mbid: String,
    placeholderIcon: ImageVector?,
    modifier: Modifier
) {
    // TODO: handle desktop thumbnail image
    PlaceholderIcon(
        modifier = modifier,
    )
}
