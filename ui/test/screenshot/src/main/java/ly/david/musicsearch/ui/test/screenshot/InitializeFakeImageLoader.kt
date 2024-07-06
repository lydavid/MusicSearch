package ly.david.musicsearch.ui.test.screenshot

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
