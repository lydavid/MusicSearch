package ly.david.musicsearch.test.image

import androidx.compose.runtime.Composable
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory

@OptIn(ExperimentalCoilApi::class)
@Composable
fun InitializeFakeImageLoader() {
    setSingletonImageLoaderFactory { context ->
        getFakeImageLoader(context)
    }
}
