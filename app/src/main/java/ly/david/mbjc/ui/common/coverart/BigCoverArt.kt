package ly.david.mbjc.ui.common.coverart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
internal fun BigCoverArt(
    modifier: Modifier = Modifier,
    coverArtUrl: String = "",
) {
    if (coverArtUrl.isNotEmpty()) {

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(coverArtUrl.useHttps())
                .size(Size.ORIGINAL)
                .scale(Scale.FIT)
                .crossfade(true)
                .build(),
            imageLoader = LocalContext.current.imageLoader
        )

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp

        when (painter.state) {
            is AsyncImagePainter.State.Loading, AsyncImagePainter.State.Empty -> {
                Box(
                    modifier = modifier
                        .height(screenWidth)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is AsyncImagePainter.State.Success -> {
                Image(
                    modifier = modifier
                        .fillMaxWidth()
                        .semantics { testTag = "coverArtImage" },
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
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
