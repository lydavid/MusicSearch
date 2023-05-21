package ly.david.ui.common.coverart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Icon
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
import ly.david.data.common.useHttps
import ly.david.data.coverart.trimCoverArtSuffix
import ly.david.ui.common.SMALL_COVER_ART_SIZE

@Composable
fun SmallCoverArt(
    coverArtUrl: String,
    modifier: Modifier = Modifier,
    placeholderIcon: ImageVector = Icons.Default.Album,
) {
    if (coverArtUrl.isNotEmpty()) {

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(coverArtUrl.useHttps())
                .size(Size(SMALL_COVER_ART_SIZE, SMALL_COVER_ART_SIZE))
                .scale(Scale.FIT)
                .crossfade(true)
                .memoryCacheKey(coverArtUrl.trimCoverArtSuffix())
                .build(),
            imageLoader = LocalContext.current.imageLoader
        )

        when (painter.state) {
            is AsyncImagePainter.State.Loading, AsyncImagePainter.State.Empty -> {
                PlaceholderIcon(modifier, placeholderIcon)
            }
            is AsyncImagePainter.State.Success -> {
                Image(
                    modifier = modifier
                        .size(SMALL_COVER_ART_SIZE.dp),
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                )
            }
            is AsyncImagePainter.State.Error -> {
                // No need to show error. List items will auto-retry when next recomposed.
                PlaceholderIcon(modifier, placeholderIcon)
            }
        }
    } else {
        PlaceholderIcon(modifier, placeholderIcon)
    }
}

@Composable
private fun PlaceholderIcon(
    modifier: Modifier = Modifier,
    placeholderIcon: ImageVector = Icons.Default.Album,
) {
    Icon(
        modifier = modifier
            .size(SMALL_COVER_ART_SIZE.dp),
        imageVector = placeholderIcon,
        contentDescription = null
    )
}
