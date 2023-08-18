package ly.david.ui.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.CorporateFare
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Piano
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.ui.graphics.vector.ImageVector
import ly.david.data.network.MusicBrainzEntity

/**
 * Returns an appropriate string resource for this [MusicBrainzEntity].
 */
@StringRes
fun MusicBrainzEntity.getDisplayTextRes(): Int {
    return when (this) {
        MusicBrainzEntity.AREA -> R.string.area
        MusicBrainzEntity.ARTIST -> R.string.artist
        MusicBrainzEntity.EVENT -> R.string.event
        MusicBrainzEntity.GENRE -> R.string.genre
        MusicBrainzEntity.INSTRUMENT -> R.string.instrument
        MusicBrainzEntity.LABEL -> R.string.label
        MusicBrainzEntity.PLACE -> R.string.place
        MusicBrainzEntity.RECORDING -> R.string.recording
        MusicBrainzEntity.RELEASE -> R.string.release
        MusicBrainzEntity.RELEASE_GROUP -> R.string.release_group
        MusicBrainzEntity.SERIES -> R.string.series
        MusicBrainzEntity.WORK -> R.string.work
        MusicBrainzEntity.URL -> R.string.url
        MusicBrainzEntity.COLLECTION -> R.string.collection
    }
}

fun MusicBrainzEntity.getIcon(): ImageVector? {
    return when (this) {
        MusicBrainzEntity.AREA -> Icons.Default.Public
        MusicBrainzEntity.ARTIST -> Icons.Default.Person
        MusicBrainzEntity.COLLECTION -> Icons.Default.CollectionsBookmark
        MusicBrainzEntity.EVENT -> Icons.Default.Event
        MusicBrainzEntity.GENRE -> Icons.Default.TheaterComedy
        MusicBrainzEntity.INSTRUMENT -> Icons.Default.Piano
        MusicBrainzEntity.LABEL -> Icons.Default.CorporateFare
        MusicBrainzEntity.PLACE -> Icons.Default.Place
        MusicBrainzEntity.RECORDING -> Icons.Default.Mic
        MusicBrainzEntity.RELEASE -> Icons.Default.Album
        MusicBrainzEntity.RELEASE_GROUP -> Icons.Default.Folder
        MusicBrainzEntity.SERIES -> Icons.Default.List
        MusicBrainzEntity.WORK -> Icons.Default.MusicNote
        MusicBrainzEntity.URL -> Icons.Default.Link
        else -> {
            // No icons.
            null
        }
    }
}
