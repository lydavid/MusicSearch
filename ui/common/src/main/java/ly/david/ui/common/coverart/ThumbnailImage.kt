package ly.david.ui.common.coverart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import ly.david.data.coverart.buildCoverArtUrl
import ly.david.data.coverart.trimCoverArtSuffix
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.SMALL_COVER_ART_SIZE
import ly.david.ui.common.getIcon
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme

@Composable
fun ThumbnailImage(
    coverArtUrl: String,
    entity: MusicBrainzResource,
    modifier: Modifier = Modifier,
) {

    val placeholderIcon = entity.getIcon()

    var _modifier = modifier.size(SMALL_COVER_ART_SIZE.dp)
    if (entity == MusicBrainzResource.ARTIST) _modifier = _modifier.clip(CircleShape)

    if (coverArtUrl.isNotEmpty()) {

        val fullCoverArtPath = if (entity == MusicBrainzResource.ARTIST) {
            coverArtUrl
        } else {
            buildCoverArtUrl(
                coverArtPath = coverArtUrl,
                thumbnail = true
            )
        }

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(fullCoverArtPath.useHttps())
                .size(Size(SMALL_COVER_ART_SIZE, SMALL_COVER_ART_SIZE))
                .scale(Scale.FIT)
                .crossfade(true)
                .memoryCacheKey(fullCoverArtPath.trimCoverArtSuffix())
                .build(),
            imageLoader = LocalContext.current.imageLoader
        )

        when (painter.state) {
            is AsyncImagePainter.State.Loading, AsyncImagePainter.State.Empty -> {
                PlaceholderIcon(_modifier, placeholderIcon)
            }

            is AsyncImagePainter.State.Success -> {
                Image(
                    modifier = _modifier,
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                )
            }

            is AsyncImagePainter.State.Error -> {
                // No need to show error. List items will auto-retry when next recomposed.
                PlaceholderIcon(_modifier, placeholderIcon)
            }
        }
    } else {
        PlaceholderIcon(_modifier, placeholderIcon)
    }
}

@Composable
private fun PlaceholderIcon(
    modifier: Modifier = Modifier,
    placeholderIcon: ImageVector? = null,
) {
    Icon(
        modifier = modifier
            .size(SMALL_COVER_ART_SIZE.dp),
        imageVector = placeholderIcon ?: Icons.Default.Album,
        contentDescription = null
    )
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            ThumbnailImage(
                coverArtUrl = "https://coverartarchive.org/release/afa0b2a6-8384-44d4-a907-76da213ca24f/25740026489",
                entity = MusicBrainzResource.RELEASE
            )
        }
    }
}
