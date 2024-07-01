package ly.david.musicsearch.ui.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import ly.david.musicsearch.core.models.common.useHttps
import ly.david.musicsearch.ui.image.internal.PlaceholderIcon
import ly.david.musicsearch.ui.core.SMALL_IMAGE_SIZE
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@Composable
actual fun ThumbnailImage(
    url: String,
    mbid: String,
    placeholderIcon: ImageVector?,
    modifier: Modifier,
) {
    val sizeModifier = modifier.size(SMALL_IMAGE_SIZE.dp)

    if (url.isNotEmpty()) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url.useHttps())
                .size(Size(SMALL_IMAGE_SIZE, SMALL_IMAGE_SIZE))
                .scale(Scale.FIT)
                .crossfade(true)
                .memoryCacheKey(mbid)
                .build(),
            imageLoader = LocalContext.current.imageLoader
        )

        when (painter.state) {
            is AsyncImagePainter.State.Loading, AsyncImagePainter.State.Empty -> {
                PlaceholderIcon(sizeModifier, placeholderIcon)
            }

            is AsyncImagePainter.State.Success -> {
                Image(
                    modifier = sizeModifier,
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                )
            }

            is AsyncImagePainter.State.Error -> {
                // No need to show error. List items will auto-retry when next recomposed.
                PlaceholderIcon(sizeModifier, placeholderIcon)
            }
        }
    } else {
        PlaceholderIcon(sizeModifier, placeholderIcon)
    }
}

@DefaultPreviews
@Composable
internal fun PreviewThumbnailImage() {
    PreviewTheme {
        Surface {
            ThumbnailImage(
                url = "https://coverartarchive.org/release/afa0b2a6-8384-44d4-a907-76da213ca24f/25740026489",
                mbid = "afa0b2a6-8384-44d4-a907-76da213ca24f",
                placeholderIcon = null
            )
        }
    }
}
