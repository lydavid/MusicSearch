package ly.david.musicsearch.ui.image

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import ly.david.musicsearch.core.models.common.useHttps

@Composable
fun LargeImage(
    url: String,
    mbid: String,
    modifier: Modifier = Modifier,
) {
    if (url.isNotEmpty()) {
        AsyncImage(
            modifier = modifier
                .fillMaxWidth()
                .testTag(LargeImageTestTag.IMAGE.name),
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(url.useHttps())
                .crossfade(true)
                .memoryCacheKey(mbid)
                .placeholderMemoryCacheKey(mbid)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )
    }
}

enum class LargeImageTestTag {
    IMAGE,
}
