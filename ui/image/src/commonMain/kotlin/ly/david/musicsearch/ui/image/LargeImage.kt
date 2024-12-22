package ly.david.musicsearch.ui.image

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import ly.david.musicsearch.shared.domain.common.useHttps

/**
 * @param placeholderKey A unique ID that we use as a cache key.
 */
@Composable
fun LargeImage(
    url: String,
    placeholderKey: String,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true,
) {
    val imageModifier = modifier.testTag(LargeImageTestTag.IMAGE.name)
        .then(
            if (isCompact) {
                Modifier.fillMaxWidth()
            } else {
                Modifier.fillMaxHeight()
            },
        )
    val contentScale = if (isCompact) {
        ContentScale.FillWidth
    } else {
        ContentScale.Fit
    }

    if (url.isNotEmpty()) {
        AsyncImage(
            modifier = imageModifier,
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(url.useHttps())
                .crossfade(true)
                .placeholderMemoryCacheKey(placeholderKey)
                .build(),
            contentDescription = null,
            contentScale = contentScale,
        )
    }
}

enum class LargeImageTestTag {
    IMAGE,
}
