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
import ly.david.mbjc.ui.navigation.Destination

/**
 * An appropriate icon for a given [destination].
 */
@Composable
internal fun DestinationIcon(
    destination: Destination,
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier,
        imageVector = when (destination) {
            Destination.LOOKUP_ARTIST -> Icons.Default.Person
            Destination.LOOKUP_RELEASE_GROUP -> Icons.Default.Folder
            Destination.LOOKUP_RELEASE -> Icons.Default.Album
            Destination.LOOKUP_RECORDING -> Icons.Default.Mic
            Destination.LOOKUP_LABEL -> Icons.Default.CorporateFare
            Destination.LOOKUP_PLACE -> Icons.Default.PinDrop
            Destination.LOOKUP_WORK -> Icons.Default.MusicNote
            else -> {
                // No icons.
                return
            }
        },
        contentDescription = ""
    )
}
