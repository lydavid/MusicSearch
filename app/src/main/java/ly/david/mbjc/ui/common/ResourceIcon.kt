package ly.david.mbjc.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.CorporateFare
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.mbjc.data.network.MusicBrainzResource

/**
 * An appropriate icon for a given [destination].
 */
@Composable
internal fun ResourceIcon(
    resource: MusicBrainzResource,
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier,
        imageVector = when (resource) {
            MusicBrainzResource.ARTIST -> Icons.Default.Person
            MusicBrainzResource.RELEASE_GROUP -> Icons.Default.Folder
            MusicBrainzResource.RELEASE -> Icons.Default.Album
            MusicBrainzResource.RECORDING -> Icons.Default.Mic
            MusicBrainzResource.LABEL -> Icons.Default.CorporateFare
            MusicBrainzResource.PLACE -> Icons.Default.PinDrop
            MusicBrainzResource.WORK -> Icons.Default.MusicNote
            else -> {
                // No icons.
                return
            }
        },
        contentDescription = ""
    )
}
