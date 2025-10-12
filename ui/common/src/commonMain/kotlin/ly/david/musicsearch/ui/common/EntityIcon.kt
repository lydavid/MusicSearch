package ly.david.musicsearch.ui.common

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

/**
 * An appropriate icon for a given [entityType].
 */
@Composable
fun EntityIcon(
    entityType: MusicBrainzEntityType,
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier,
        imageVector = entityType.getIcon() ?: return,
        contentDescription = null,
    )
}
