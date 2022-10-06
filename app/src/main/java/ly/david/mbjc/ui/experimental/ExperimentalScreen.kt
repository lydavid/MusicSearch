package ly.david.mbjc.ui.experimental

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import ly.david.mbjc.ui.common.useHttps

@Composable
internal fun ExperimentalScreen() {

    val url = "http://coverartarchive.org/release/d3d5e00c-7c26-47e4-96e0-4b2f359ef035/33435064249-250.jpg"
    SubcomposeAsyncImage(
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth,
        model = url.useHttps(),
        contentDescription = null,
        loading = { CircularProgressIndicator() }
    )
}
