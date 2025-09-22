package ly.david.musicsearch.ui.common.image

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.slack.circuit.sharedelements.SharedElementTransitionScope
import ly.david.musicsearch.shared.domain.common.prependHttps
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.ui.common.icons.CheckCircle
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.listitem.ListItemSharedTransitionKey
import ly.david.musicsearch.ui.common.theme.SMALL_IMAGE_SIZE

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ThumbnailImage(
    url: String,
    imageId: ImageId?,
    placeholderIcon: ImageVector?,
    modifier: Modifier = Modifier,
    clipCircle: Boolean = false,
    isSelected: Boolean = false,
    size: Dp = SMALL_IMAGE_SIZE.dp,
) {
    SharedElementTransitionScope {
        val resizeModifier = modifier.size(size)
        val modifierWithSharedBounds = if (imageId == null) {
            resizeModifier
        } else {
            resizeModifier.sharedBounds(
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
        val clippedModifier = if (clipCircle) {
            modifierWithSharedBounds.clip(CircleShape)
        } else {
            modifierWithSharedBounds
        }

        val placeholder = rememberVectorPainter(placeholderIcon ?: DefaultPlaceholderImageVector)
        when {
            isSelected -> {
                Icon(
                    modifier = clippedModifier,
                    imageVector = CustomIcons.CheckCircle,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "selected",
                )
            }

            url.isNotEmpty() -> {
                AsyncImage(
                    modifier = clippedModifier,
                    placeholder = forwardingPainter(
                        painter = placeholder,
                        colorFilter = ColorFilter.tint(LocalContentColor.current),
                    ),
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(url.prependHttps())
                        .scale(Scale.FILL)
                        .crossfade(true)
                        .memoryCacheKey(imageId?.value?.toString())
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }

            else -> {
                PlaceholderIcon(
                    modifier = clippedModifier,
                    placeholderIcon = placeholderIcon,
                )
            }
        }
    }
}
