package ly.david.musicsearch.ui.common.image

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
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
import com.slack.circuit.sharedelements.SharedElementTransitionScope
import ly.david.musicsearch.shared.domain.common.prependHttps
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.ui.common.listitem.ListItemSharedTransitionKey
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

/**
 * @param imageId A unique ID that we use as a cache key.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun LargeImage(
    url: String,
    imageId: ImageId?,
    modifier: Modifier = Modifier,
    isCompact: Boolean = true,
    zoomEnabled: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    SharedElementTransitionScope {
        val zoomState = rememberZoomState(
            maxScale = 8f,
        )

        val imageModifier = modifier.testTag(LargeImageTestTag.IMAGE.name)
            .then(
                if (isCompact) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier.fillMaxHeight()
                },
            )
        val modifierWithSharedBounds = if (imageId == null) {
            imageModifier
        } else {
            imageModifier.sharedBounds(
                sharedContentState = rememberSharedContentState(
                    ListItemSharedTransitionKey(
                        imageId = imageId,
                        type = ListItemSharedTransitionKey.ElementType.Image,
                    ),
                ),
                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(),
                animatedVisibilityScope = requireAnimatedScope(
                    SharedElementTransitionScope.AnimatedScope.Navigation,
                ),
            )
        }
        val zoomableImageModifier = modifierWithSharedBounds
            .zoomable(
                zoomState = zoomState,
                zoomEnabled = zoomEnabled,
                onDoubleTap = { position ->
                    if (!zoomEnabled) return@zoomable

                    val targetScale = when {
                        zoomState.scale < 4f -> 4f
                        zoomState.scale < 8f -> 8f
                        else -> 1f
                    }
                    zoomState.changeScale(targetScale, position)
                },
            )
        val clickableZoomableImageModifier = if (onClick == null) {
            zoomableImageModifier
        } else {
            zoomableImageModifier
                // This gives us the ripple feedback
                .clickable(onClick = onClick)
        }

        val contentScale = if (isCompact) {
            ContentScale.FillWidth
        } else {
            ContentScale.Fit
        }

        if (url.isNotEmpty()) {
            AsyncImage(
                modifier = clickableZoomableImageModifier,
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(url.prependHttps())
                    .crossfade(true)
                    .placeholderMemoryCacheKey(imageId?.value?.toString().orEmpty())
                    .build(),
                contentDescription = null,
                contentScale = contentScale,
            )
        }
    }
}

enum class LargeImageTestTag {
    IMAGE,
}
