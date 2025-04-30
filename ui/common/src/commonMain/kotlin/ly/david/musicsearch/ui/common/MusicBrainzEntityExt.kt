package ly.david.musicsearch.ui.common

import androidx.compose.ui.graphics.vector.ImageVector
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.common.icons.Album
import ly.david.musicsearch.ui.common.icons.CollectionsBookmark
import ly.david.musicsearch.ui.common.icons.CorporateFare
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Event
import ly.david.musicsearch.ui.common.icons.Folder
import ly.david.musicsearch.ui.common.icons.Link
import ly.david.musicsearch.ui.common.icons.List
import ly.david.musicsearch.ui.common.icons.Mic
import ly.david.musicsearch.ui.common.icons.MusicNote
import ly.david.musicsearch.ui.common.icons.Person
import ly.david.musicsearch.ui.common.icons.Piano
import ly.david.musicsearch.ui.common.icons.Place
import ly.david.musicsearch.ui.common.icons.Public
import ly.david.musicsearch.ui.common.icons.TheaterComedy

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
        MusicBrainzEntity.AREA -> CustomIcons.Public
        MusicBrainzEntity.ARTIST -> CustomIcons.Person
        MusicBrainzEntity.COLLECTION -> CustomIcons.CollectionsBookmark
        MusicBrainzEntity.EVENT -> CustomIcons.Event
        MusicBrainzEntity.GENRE -> CustomIcons.TheaterComedy
        MusicBrainzEntity.INSTRUMENT -> CustomIcons.Piano
        MusicBrainzEntity.LABEL -> CustomIcons.CorporateFare
        MusicBrainzEntity.PLACE -> CustomIcons.Place
        MusicBrainzEntity.RECORDING -> CustomIcons.Mic
        MusicBrainzEntity.RELEASE -> CustomIcons.Album
        MusicBrainzEntity.RELEASE_GROUP -> CustomIcons.Folder
        MusicBrainzEntity.SERIES -> CustomIcons.List
        MusicBrainzEntity.WORK -> CustomIcons.MusicNote
        MusicBrainzEntity.URL -> CustomIcons.Link
        else -> {
            // No icons.
            null
        }
    }
}
