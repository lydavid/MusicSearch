package ly.david.musicsearch.ui.common

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

/**
 * An appropriate icon for a given [entity].
 */
@Composable
fun EntityIcon(
    entity: MusicBrainzEntityType,
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = modifier,
        imageVector = entity.getIcon() ?: return,
        contentDescription = null,
    )
}
