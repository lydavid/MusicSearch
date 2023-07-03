package ly.david.ui.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.CollectionsBookmark
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
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.ui.graphics.vector.ImageVector
import ly.david.data.network.MusicBrainzResource

/**
 * Returns an appropriate string resource for this [MusicBrainzResource].
 */
@StringRes
fun MusicBrainzResource.getDisplayTextRes(): Int {
    return when (this) {
        MusicBrainzResource.AREA -> R.string.area
        MusicBrainzResource.ARTIST -> R.string.artist
        MusicBrainzResource.EVENT -> R.string.event
        MusicBrainzResource.GENRE -> R.string.genre
        MusicBrainzResource.INSTRUMENT -> R.string.instrument
        MusicBrainzResource.LABEL -> R.string.label
        MusicBrainzResource.PLACE -> R.string.place
        MusicBrainzResource.RECORDING -> R.string.recording
        MusicBrainzResource.RELEASE -> R.string.release
        MusicBrainzResource.RELEASE_GROUP -> R.string.release_group
        MusicBrainzResource.SERIES -> R.string.series
        MusicBrainzResource.WORK -> R.string.work
        MusicBrainzResource.URL -> R.string.url
        MusicBrainzResource.COLLECTION -> R.string.collection
    }
}

fun MusicBrainzResource.getIcon(): ImageVector {
    return when (this) {
        MusicBrainzResource.AREA -> Icons.Default.Public
        MusicBrainzResource.ARTIST -> Icons.Default.Person
        MusicBrainzResource.COLLECTION -> Icons.Default.CollectionsBookmark
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
            Icons.Default.QuestionMark
        }
    }
}
