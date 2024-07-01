package ly.david.musicsearch.ui.image

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewLightDark
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ly.david.musicsearch.core.models.common.useHttps
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@Composable
actual fun LargeImage(
    url: String,
    mbid: String,
    modifier: Modifier,
) {
    if (url.isNotEmpty()) {
        AsyncImage(
            modifier = modifier
                .fillMaxWidth()
                .testTag(LargeImageTestTag.IMAGE.name),
            model = ImageRequest.Builder(LocalContext.current)
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

// Only see loading spinner in IDE, but works if we run the preview.
@PreviewLightDark
@Composable
internal fun PreviewLargeImage() {
    PreviewTheme {
        Surface {
            LargeImage(
                url = "https://coverartarchive.org/release/afa0b2a6-8384-44d4-a907-76da213ca24f/25740026489",
                mbid = "afa0b2a6-8384-44d4-a907-76da213ca24f",
            )
        }
    }
}
