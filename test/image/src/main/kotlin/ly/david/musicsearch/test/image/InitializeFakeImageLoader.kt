package ly.david.musicsearch.test.image

import androidx.compose.runtime.Composable
import coil3.compose.setSingletonImageLoaderFactory

@Composable
fun InitializeFakeImageLoader() {
    setSingletonImageLoaderFactory { context ->
        getFakeImageLoader(context)
    }
}
