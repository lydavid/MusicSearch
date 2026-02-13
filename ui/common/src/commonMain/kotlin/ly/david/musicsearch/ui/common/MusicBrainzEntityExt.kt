package ly.david.musicsearch.ui.common

import androidx.compose.ui.graphics.vector.ImageVector
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
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
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.area
import musicsearch.ui.common.generated.resources.areas
import musicsearch.ui.common.generated.resources.artist
import musicsearch.ui.common.generated.resources.artists
import musicsearch.ui.common.generated.resources.collection
import musicsearch.ui.common.generated.resources.collections
import musicsearch.ui.common.generated.resources.event
import musicsearch.ui.common.generated.resources.events
import musicsearch.ui.common.generated.resources.genre
import musicsearch.ui.common.generated.resources.genres
import musicsearch.ui.common.generated.resources.instrument
import musicsearch.ui.common.generated.resources.instruments
import musicsearch.ui.common.generated.resources.label
import musicsearch.ui.common.generated.resources.labels
import musicsearch.ui.common.generated.resources.place
import musicsearch.ui.common.generated.resources.places
import musicsearch.ui.common.generated.resources.recording
import musicsearch.ui.common.generated.resources.recordings
import musicsearch.ui.common.generated.resources.release
import musicsearch.ui.common.generated.resources.releaseGroup
import musicsearch.ui.common.generated.resources.releaseGroups
import musicsearch.ui.common.generated.resources.releases
import musicsearch.ui.common.generated.resources.series
import musicsearch.ui.common.generated.resources.url
import musicsearch.ui.common.generated.resources.urls
import musicsearch.ui.common.generated.resources.work
import musicsearch.ui.common.generated.resources.works
import org.jetbrains.compose.resources.StringResource

/**
 * Returns an appropriate name for this [MusicBrainzEntityType].
 */
fun MusicBrainzEntityType.getName(): StringResource {
    return when (this) {
        MusicBrainzEntityType.AREA -> Res.string.area
        MusicBrainzEntityType.ARTIST -> Res.string.artist
        MusicBrainzEntityType.EVENT -> Res.string.event
        MusicBrainzEntityType.GENRE -> Res.string.genre
        MusicBrainzEntityType.INSTRUMENT -> Res.string.instrument
        MusicBrainzEntityType.LABEL -> Res.string.label
        MusicBrainzEntityType.PLACE -> Res.string.place
        MusicBrainzEntityType.RECORDING -> Res.string.recording
        MusicBrainzEntityType.RELEASE -> Res.string.release
        MusicBrainzEntityType.RELEASE_GROUP -> Res.string.releaseGroup
        MusicBrainzEntityType.SERIES -> Res.string.series
        MusicBrainzEntityType.WORK -> Res.string.work
        MusicBrainzEntityType.URL -> Res.string.url
        MusicBrainzEntityType.COLLECTION -> Res.string.collection
    }
}

fun MusicBrainzEntityType.getNamePlural(): StringResource {
    return when (this) {
        MusicBrainzEntityType.AREA -> Res.string.areas
        MusicBrainzEntityType.ARTIST -> Res.string.artists
        MusicBrainzEntityType.EVENT -> Res.string.events
        MusicBrainzEntityType.GENRE -> Res.string.genres
        MusicBrainzEntityType.INSTRUMENT -> Res.string.instruments
        MusicBrainzEntityType.LABEL -> Res.string.labels
        MusicBrainzEntityType.PLACE -> Res.string.places
        MusicBrainzEntityType.RECORDING -> Res.string.recordings
        MusicBrainzEntityType.RELEASE -> Res.string.releases
        MusicBrainzEntityType.RELEASE_GROUP -> Res.string.releaseGroups
        MusicBrainzEntityType.SERIES -> Res.string.series
        MusicBrainzEntityType.WORK -> Res.string.works
        MusicBrainzEntityType.URL -> Res.string.urls
        MusicBrainzEntityType.COLLECTION -> Res.string.collections
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
