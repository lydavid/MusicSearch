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
import ly.david.musicsearch.shared.domain.common.useHttps
import ly.david.musicsearch.ui.common.icons.CheckCircle
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.listitem.ListItemSharedTransitionKey
import ly.david.musicsearch.ui.core.SMALL_IMAGE_SIZE

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ThumbnailImage(
    url: String,
    placeholderKey: String,
    placeholderIcon: ImageVector?,
    modifier: Modifier = Modifier,
    clipCircle: Boolean = false,
    isSelected: Boolean = false,
    size: Dp = SMALL_IMAGE_SIZE.dp,
) {
    SharedElementTransitionScope {
        val resizeModifier = modifier.size(size)
        // TODO: Consider a value class for image database id which is used as placeholderKey
        val modifierWithSharedBounds = if (placeholderKey.isEmpty() || placeholderKey == 0L.toString()) {
            resizeModifier
        } else {
            resizeModifier.sharedBounds(
                sharedContentState = rememberSharedContentState(
                    ListItemSharedTransitionKey(
                        id = placeholderKey,
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
                        .data(url.useHttps())
                        .scale(Scale.FILL)
                        .crossfade(true)
                        .memoryCacheKey(placeholderKey)
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
