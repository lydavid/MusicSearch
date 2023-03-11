package ly.david.mbjc.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.CorporateFare
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Piano
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.network.MusicBrainzResource

/**
 * An appropriate icon for a given [resource].
 */
@Composable
internal fun ResourceIcon(
    resource: MusicBrainzResource,
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier,
        imageVector = when (resource) {
            MusicBrainzResource.AREA -> Icons.Default.Public
            MusicBrainzResource.ARTIST -> Icons.Default.Person
            MusicBrainzResource.EVENT -> Icons.Default.Event
            MusicBrainzResource.GENRE -> Icons.Default.TheaterComedy
            MusicBrainzResource.INSTRUMENT -> Icons.Default.Piano
            MusicBrainzResource.LABEL -> Icons.Default.CorporateFare
            MusicBrainzResource.PLACE -> Icons.Default.Place
            MusicBrainzResource.RECORDING -> Icons.Default.Mic
            MusicBrainzResource.RELEASE -> Icons.Default.Album
            MusicBrainzResource.RELEASE_GROUP -> Icons.Default.Folder
            MusicBrainzResource.SERIES -> Icons.Default.List
            MusicBrainzResource.WORK -> Icons.Default.MusicNote
            else -> {
                // Non-searchable resources like Genre, URL
                // No icons.
                return
            }
        },
        contentDescription = null
    )
}
