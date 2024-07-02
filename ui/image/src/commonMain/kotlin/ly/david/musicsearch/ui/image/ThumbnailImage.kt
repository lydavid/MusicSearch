package ly.david.musicsearch.ui.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import coil3.size.Size
import ly.david.musicsearch.core.models.common.useHttps
import ly.david.musicsearch.ui.core.SMALL_IMAGE_SIZE
import ly.david.musicsearch.ui.image.internal.PlaceholderIcon

@Composable
fun ThumbnailImage(
    url: String,
    mbid: String,
    placeholderIcon: ImageVector?,
    modifier: Modifier = Modifier,
) {
    val sizeModifier = modifier.size(SMALL_IMAGE_SIZE.dp)

    if (url.isNotEmpty()) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(url.useHttps())
                .size(
                    Size(
                        SMALL_IMAGE_SIZE,
                        SMALL_IMAGE_SIZE,
                    ),
                )
                .scale(Scale.FIT)
                .crossfade(true)
                .memoryCacheKey(mbid)
                .build(),
        )
        val state by painter.state.collectAsState()

        when (state) {
            is AsyncImagePainter.State.Loading, AsyncImagePainter.State.Empty -> {
                PlaceholderIcon(
                    sizeModifier,
                    placeholderIcon,
                )
            }

            is AsyncImagePainter.State.Success -> {
                state.painter?.let {
                    Image(
                        modifier = sizeModifier,
                        painter = it,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                    )
                } ?: run {
                    PlaceholderIcon(
                        sizeModifier,
                        placeholderIcon,
                    )
                }
            }

            is AsyncImagePainter.State.Error -> {
                // No need to show error. List items will auto-retry when next recomposed.
                PlaceholderIcon(
                    sizeModifier,
                    placeholderIcon,
                )
            }
        }
    } else {
        PlaceholderIcon(
            sizeModifier,
            placeholderIcon,
        )
    }
}
