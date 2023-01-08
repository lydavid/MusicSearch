package ly.david.mbjc.ui.common.coverart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
internal fun SmallCoverArt(
    modifier: Modifier = Modifier,
    coverArtUrl: String = "",
) {
    if (coverArtUrl.isNotEmpty()) {

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(coverArtUrl.useHttps())
                .size(Size(64, 64))
                .scale(Scale.FIT)
                .crossfade(true)
                .build(),
            imageLoader = LocalContext.current.imageLoader
        )

        when (painter.state) {
            is AsyncImagePainter.State.Loading, AsyncImagePainter.State.Empty -> {
                Box(
                    modifier = modifier
                        .size(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is AsyncImagePainter.State.Success -> {
                Image(
                    modifier = modifier
                        .size(64.dp)
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
    } else {
        // Used just so that constraint layout can link to.
        Spacer(modifier = modifier)
    }
}
