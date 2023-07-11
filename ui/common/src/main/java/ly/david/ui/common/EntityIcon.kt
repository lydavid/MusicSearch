package ly.david.ui.common

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.network.MusicBrainzEntity

/**
 * An appropriate icon for a given [entity].
 */
@Composable
fun EntityIcon(
    entity: MusicBrainzEntity,
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier,
        imageVector = entity.getIcon() ?: return,
        contentDescription = null
    )
}
