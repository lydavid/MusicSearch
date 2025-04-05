package ly.david.musicsearch.ui.common

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
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.strings.AppStrings

/**
 * Returns an appropriate name for this [MusicBrainzEntity].
 */
fun MusicBrainzEntity.getName(strings: AppStrings): String {
    return when (this) {
        MusicBrainzEntity.AREA -> strings.area
        MusicBrainzEntity.ARTIST -> strings.artist
        MusicBrainzEntity.EVENT -> strings.event
        MusicBrainzEntity.GENRE -> strings.genre
        MusicBrainzEntity.INSTRUMENT -> strings.instrument
        MusicBrainzEntity.LABEL -> strings.label
        MusicBrainzEntity.PLACE -> strings.place
        MusicBrainzEntity.RECORDING -> strings.recording
        MusicBrainzEntity.RELEASE -> strings.release
        MusicBrainzEntity.RELEASE_GROUP -> strings.releaseGroup
        MusicBrainzEntity.SERIES -> strings.series
        MusicBrainzEntity.WORK -> strings.work
        MusicBrainzEntity.URL -> strings.url
        MusicBrainzEntity.COLLECTION -> strings.collection
    }
}

fun MusicBrainzEntity.getNamePlural(strings: AppStrings): String {
    return when (this) {
        MusicBrainzEntity.AREA -> strings.areas
        MusicBrainzEntity.ARTIST -> strings.artists
        MusicBrainzEntity.EVENT -> strings.events
        MusicBrainzEntity.GENRE -> strings.genres
        MusicBrainzEntity.INSTRUMENT -> strings.instruments
        MusicBrainzEntity.LABEL -> strings.labels
        MusicBrainzEntity.PLACE -> strings.places
        MusicBrainzEntity.RECORDING -> strings.recordings
        MusicBrainzEntity.RELEASE -> strings.releases
        MusicBrainzEntity.RELEASE_GROUP -> strings.releaseGroups
        MusicBrainzEntity.SERIES -> strings.series
        MusicBrainzEntity.WORK -> strings.works
        MusicBrainzEntity.URL -> strings.urls
        MusicBrainzEntity.COLLECTION -> strings.collections
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
