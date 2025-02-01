package ly.david.musicsearch.ui.image

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import ly.david.musicsearch.shared.domain.common.useHttps
import ly.david.musicsearch.ui.core.SMALL_IMAGE_SIZE
import ly.david.musicsearch.ui.image.internal.PlaceholderIcon

@Composable
fun ThumbnailImage(
    url: String,
    placeholderKey: String,
    placeholderIcon: ImageVector?,
    modifier: Modifier = Modifier,
    size: Dp = SMALL_IMAGE_SIZE.dp,
) {
    val sizeModifier = modifier.size(size)
    val placeholder = rememberVectorPainter(placeholderIcon ?: Icons.Default.Album)

    if (url.isNotEmpty()) {
        AsyncImage(
            modifier = sizeModifier,
            placeholder = forwardingPainter(
                painter = placeholder,
                colorFilter = ColorFilter.tint(LocalContentColor.current),
            ),
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(url.useHttps())
                .scale(Scale.FIT)
                .crossfade(true)
                .memoryCacheKey(placeholderKey)
                .build(),
            contentDescription = null,
        )
    } else {
        PlaceholderIcon(
            modifier = sizeModifier,
            placeholderIcon = placeholderIcon,
        )
    }
}
