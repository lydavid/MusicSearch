package ly.david.musicsearch.ui.common.topappbar

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.strings.AppStrings

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

fun Tab.getTitle(strings: AppStrings): String {
    return when (this) {
        Tab.DETAILS -> strings.details
        Tab.AREAS -> strings.areas
        Tab.ARTISTS -> strings.artists
        Tab.EVENTS -> strings.events
        Tab.GENRES -> strings.genres
        Tab.INSTRUMENTS -> strings.instruments
        Tab.LABELS -> strings.labels
        Tab.PLACES -> strings.places
        Tab.RECORDINGS -> strings.recordings
        Tab.RELATIONSHIPS -> strings.relationships
        Tab.RELEASES -> strings.releases
        Tab.RELEASE_GROUPS -> strings.releaseGroups
        Tab.SERIES -> strings.series
        Tab.TRACKS -> strings.tracks
        Tab.WORKS -> strings.works
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
