package ly.david.musicsearch.ui.image.internal

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ly.david.ui.core.SMALL_IMAGE_SIZE

@Composable
internal fun PlaceholderIcon(
    modifier: Modifier = Modifier,
    placeholderIcon: ImageVector? = null,
) {
    Icon(
        modifier = modifier
            .size(SMALL_IMAGE_SIZE.dp),
        imageVector = placeholderIcon ?: Icons.Default.Album,
        contentDescription = null,
    )
}
