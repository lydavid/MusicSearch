package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.areas
import musicsearch.ui.common.generated.resources.artists
import musicsearch.ui.common.generated.resources.details
import musicsearch.ui.common.generated.resources.events
import musicsearch.ui.common.generated.resources.genres
import musicsearch.ui.common.generated.resources.instruments
import musicsearch.ui.common.generated.resources.labels
import musicsearch.ui.common.generated.resources.places
import musicsearch.ui.common.generated.resources.recordings
import musicsearch.ui.common.generated.resources.relationships
import musicsearch.ui.common.generated.resources.releaseGroups
import musicsearch.ui.common.generated.resources.releases
import musicsearch.ui.common.generated.resources.series
import musicsearch.ui.common.generated.resources.tracks
import musicsearch.ui.common.generated.resources.works
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * All possible tabs in MusicBrainz resource pages.
 */
enum class Tab {
    DETAILS,
    AREAS,
    ARTISTS,
    EVENTS,
    GENRES,
    INSTRUMENTS,
    LABELS,
    PLACES,
    RECORDINGS,
    RELATIONSHIPS,
    RELEASES,
    RELEASE_GROUPS,
    SERIES,
    TRACKS,
    WORKS,
}

@Composable
fun Tab.getTitle(): String {
    return stringResource(getTitleResource())
}

private fun Tab.getTitleResource(): StringResource {
    return when (this) {
        Tab.DETAILS -> Res.string.details
        Tab.AREAS -> Res.string.areas
        Tab.ARTISTS -> Res.string.artists
        Tab.EVENTS -> Res.string.events
        Tab.GENRES -> Res.string.genres
        Tab.INSTRUMENTS -> Res.string.instruments
        Tab.LABELS -> Res.string.labels
        Tab.PLACES -> Res.string.places
        Tab.RECORDINGS -> Res.string.recordings
        Tab.RELATIONSHIPS -> Res.string.relationships
        Tab.RELEASES -> Res.string.releases
        Tab.RELEASE_GROUPS -> Res.string.releaseGroups
        Tab.SERIES -> Res.string.series
        Tab.TRACKS -> Res.string.tracks
        Tab.WORKS -> Res.string.works
    }
}

fun Tab.toMusicBrainzEntityType(): MusicBrainzEntityType? {
    return when (this) {
        Tab.AREAS -> MusicBrainzEntityType.AREA
        Tab.ARTISTS -> MusicBrainzEntityType.ARTIST
        Tab.EVENTS -> MusicBrainzEntityType.EVENT
        Tab.GENRES -> MusicBrainzEntityType.GENRE
        Tab.INSTRUMENTS -> MusicBrainzEntityType.INSTRUMENT
        Tab.LABELS -> MusicBrainzEntityType.LABEL
        Tab.PLACES -> MusicBrainzEntityType.PLACE
        Tab.RECORDINGS -> MusicBrainzEntityType.RECORDING
        Tab.RELEASES -> MusicBrainzEntityType.RELEASE
        Tab.RELEASE_GROUPS -> MusicBrainzEntityType.RELEASE_GROUP
        Tab.SERIES -> MusicBrainzEntityType.SERIES
        Tab.WORKS -> MusicBrainzEntityType.WORK
        Tab.DETAILS,
        Tab.RELATIONSHIPS,
        Tab.TRACKS,
        -> null
    }
}

fun MusicBrainzEntityType.toTab(): Tab? {
    return when (this) {
        MusicBrainzEntityType.AREA -> Tab.AREAS
        MusicBrainzEntityType.ARTIST -> Tab.ARTISTS
        MusicBrainzEntityType.EVENT -> Tab.EVENTS
        MusicBrainzEntityType.GENRE -> Tab.GENRES
        MusicBrainzEntityType.INSTRUMENT -> Tab.INSTRUMENTS
        MusicBrainzEntityType.LABEL -> Tab.LABELS
        MusicBrainzEntityType.PLACE -> Tab.PLACES
        MusicBrainzEntityType.RECORDING -> Tab.RECORDINGS
        MusicBrainzEntityType.RELEASE -> Tab.RELEASES
        MusicBrainzEntityType.RELEASE_GROUP -> Tab.RELEASE_GROUPS
        MusicBrainzEntityType.SERIES -> Tab.SERIES
        MusicBrainzEntityType.WORK -> Tab.WORKS
        MusicBrainzEntityType.COLLECTION,
        MusicBrainzEntityType.URL,
        -> null
    }
}
