package ly.david.ui.common.coverart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import ly.david.data.common.useHttps
import ly.david.data.coverart.trimCoverArtSuffix
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme

/**
 * A big cover art that fills the screen's width.
 */
@Composable
fun BigCoverArt(
    coverArtUrl: String,
    modifier: Modifier = Modifier,
) {
    if (coverArtUrl.isNotEmpty()) {

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(coverArtUrl.useHttps())
                .size(Size.ORIGINAL)
                .scale(Scale.FIT)
                .crossfade(true)
                .memoryCacheKey(coverArtUrl.trimCoverArtSuffix())
                .placeholderMemoryCacheKey(coverArtUrl.trimCoverArtSuffix())
                .build(),
            imageLoader = LocalContext.current.imageLoader
        )

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        when (painter.state) {
            is AsyncImagePainter.State.Loading, AsyncImagePainter.State.Empty -> {
                if (painter.state.painter == null) {
                    Box(
                        modifier = modifier
                            .height(screenWidth)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    PainterImage(
                        modifier = modifier,
                        painter = painter
                    )
                }
            }
            is AsyncImagePainter.State.Success -> {
                PainterImage(
                    modifier = modifier,
                    painter = painter
                )
            }
            is AsyncImagePainter.State.Error -> {
                // TODO: handle error with retry
                //  this case means there is an image but we failed to get it
                //  This is not on failing cover art archive lookup, but on failing downloading the image itself
            }
        }
    }
}

@Composable
private fun PainterImage(
    painter: Painter,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .fillMaxWidth()
            .semantics { testTag = "coverArtImage" },
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
    )
}

// Only see loading spinner in IDE, but works if we run the preview.
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            BigCoverArt("https://coverartarchive.org/release/afa0b2a6-8384-44d4-a907-76da213ca24f/25740026489")
        }
    }
}
