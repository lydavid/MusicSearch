package ly.david.musicsearch.ui.common.image

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.icons.Album
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.core.SMALL_IMAGE_SIZE

@Composable
internal fun PlaceholderIcon(
    modifier: Modifier = Modifier,
    placeholderIcon: ImageVector? = null,
) {
    Icon(
        modifier = modifier
            .size(SMALL_IMAGE_SIZE.dp),
        imageVector = placeholderIcon ?: DefaultPlaceholderImageVector,
        contentDescription = null,
    )
}

internal val DefaultPlaceholderImageVector = CustomIcons.Album
