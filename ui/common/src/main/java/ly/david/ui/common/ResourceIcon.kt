package ly.david.ui.common

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.network.MusicBrainzResource

/**
 * An appropriate icon for a given [resource].
 */
@Composable
fun ResourceIcon(
    resource: MusicBrainzResource,
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier,
        imageVector = resource.getIcon(),
        contentDescription = null
    )
}
