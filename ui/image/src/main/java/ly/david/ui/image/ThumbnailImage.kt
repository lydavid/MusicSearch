package ly.david.ui.image

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import ly.david.data.common.useHttps
import ly.david.ui.core.SMALL_IMAGE_SIZE
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
fun ThumbnailImage(
    url: String,
    mbid: String,
    placeholderIcon: ImageVector?,
    modifier: Modifier = Modifier,
) {

    val sizeModifier = modifier.size(SMALL_IMAGE_SIZE.dp)

    if (url.isNotEmpty()) {

        CoilImage(
            imageModel = { url.useHttps() },
            modifier = sizeModifier,
            component = rememberImageComponent {
                +ShimmerPlugin(
                    baseColor = MaterialTheme.colorScheme.surface,
                    highlightColor = MaterialTheme.colorScheme.onSurface
                )
            }
        )
//        val painter = rememberAsyncImagePainter(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(url.useHttps())
//                .size(Size(SMALL_IMAGE_SIZE, SMALL_IMAGE_SIZE))
//                .scale(Scale.FIT)
//                .crossfade(true)
//                .memoryCacheKey(mbid)
//                .build(),
//            imageLoader = LocalContext.current.imageLoader
//        )
//
//        when (painter.state) {
//            is AsyncImagePainter.State.Loading, AsyncImagePainter.State.Empty -> {
//                PlaceholderIcon(sizeModifier, placeholderIcon)
//            }
//
//            is AsyncImagePainter.State.Success -> {
//                Image(
//                    modifier = sizeModifier,
//                    painter = painter,
//                    contentDescription = null,
//                    contentScale = ContentScale.FillWidth,
//                )
//            }
//
//            is AsyncImagePainter.State.Error -> {
//                // No need to show error. List items will auto-retry when next recomposed.
//                PlaceholderIcon(sizeModifier, placeholderIcon)
//            }
//        }
    } else {
        PlaceholderIcon(sizeModifier, placeholderIcon)
    }
}

@Composable
private fun PlaceholderIcon(
    modifier: Modifier = Modifier,
    placeholderIcon: ImageVector? = null,
) {
    Icon(
        modifier = modifier
            .size(SMALL_IMAGE_SIZE.dp),
        imageVector = placeholderIcon ?: Icons.Default.Album,
        contentDescription = null
    )
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
