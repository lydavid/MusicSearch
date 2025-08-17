package ly.david.musicsearch.ui.common

import androidx.compose.ui.graphics.vector.ImageVector
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
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
 * Returns an appropriate name for this [MusicBrainzEntityType].
 */
fun MusicBrainzEntityType.getName(strings: AppStrings): String {
    return when (this) {
        MusicBrainzEntityType.AREA -> strings.area
        MusicBrainzEntityType.ARTIST -> strings.artist
        MusicBrainzEntityType.EVENT -> strings.event
        MusicBrainzEntityType.GENRE -> strings.genre
        MusicBrainzEntityType.INSTRUMENT -> strings.instrument
        MusicBrainzEntityType.LABEL -> strings.label
        MusicBrainzEntityType.PLACE -> strings.place
        MusicBrainzEntityType.RECORDING -> strings.recording
        MusicBrainzEntityType.RELEASE -> strings.release
        MusicBrainzEntityType.RELEASE_GROUP -> strings.releaseGroup
        MusicBrainzEntityType.SERIES -> strings.series
        MusicBrainzEntityType.WORK -> strings.work
        MusicBrainzEntityType.URL -> strings.url
        MusicBrainzEntityType.COLLECTION -> strings.collection
    }
}

fun MusicBrainzEntityType.getNamePlural(strings: AppStrings): String {
    return when (this) {
        MusicBrainzEntityType.AREA -> strings.areas
        MusicBrainzEntityType.ARTIST -> strings.artists
        MusicBrainzEntityType.EVENT -> strings.events
        MusicBrainzEntityType.GENRE -> strings.genres
        MusicBrainzEntityType.INSTRUMENT -> strings.instruments
        MusicBrainzEntityType.LABEL -> strings.labels
        MusicBrainzEntityType.PLACE -> strings.places
        MusicBrainzEntityType.RECORDING -> strings.recordings
        MusicBrainzEntityType.RELEASE -> strings.releases
        MusicBrainzEntityType.RELEASE_GROUP -> strings.releaseGroups
        MusicBrainzEntityType.SERIES -> strings.series
        MusicBrainzEntityType.WORK -> strings.works
        MusicBrainzEntityType.URL -> strings.urls
        MusicBrainzEntityType.COLLECTION -> strings.collections
    }
}

fun MusicBrainzEntityType.getIcon(): ImageVector? {
    return when (this) {
        MusicBrainzEntityType.AREA -> CustomIcons.Public
        MusicBrainzEntityType.ARTIST -> CustomIcons.Person
        MusicBrainzEntityType.COLLECTION -> CustomIcons.CollectionsBookmark
        MusicBrainzEntityType.EVENT -> CustomIcons.Event
        MusicBrainzEntityType.GENRE -> CustomIcons.TheaterComedy
        MusicBrainzEntityType.INSTRUMENT -> CustomIcons.Piano
        MusicBrainzEntityType.LABEL -> CustomIcons.CorporateFare
        MusicBrainzEntityType.PLACE -> CustomIcons.Place
        MusicBrainzEntityType.RECORDING -> CustomIcons.Mic
        MusicBrainzEntityType.RELEASE -> CustomIcons.Album
        MusicBrainzEntityType.RELEASE_GROUP -> CustomIcons.Folder
        MusicBrainzEntityType.SERIES -> CustomIcons.List
        MusicBrainzEntityType.WORK -> CustomIcons.MusicNote
        MusicBrainzEntityType.URL -> CustomIcons.Link
        else -> {
            // No icons.
            null
        }
    }
}
