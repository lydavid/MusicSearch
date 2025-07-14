package ly.david.musicsearch.ui.common.topappbar

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
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
    STATS,
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
        Tab.STATS -> strings.stats
        Tab.TRACKS -> strings.tracks
        Tab.WORKS -> strings.works
    }
}

fun Tab.toMusicBrainzEntity(): MusicBrainzEntity? {
    return when (this) {
        Tab.AREAS -> MusicBrainzEntity.AREA
        Tab.ARTISTS -> MusicBrainzEntity.ARTIST
        Tab.EVENTS -> MusicBrainzEntity.EVENT
        Tab.GENRES -> MusicBrainzEntity.GENRE
        Tab.INSTRUMENTS -> MusicBrainzEntity.INSTRUMENT
        Tab.LABELS -> MusicBrainzEntity.LABEL
        Tab.PLACES -> MusicBrainzEntity.PLACE
        Tab.RECORDINGS -> MusicBrainzEntity.RECORDING
        Tab.RELEASES -> MusicBrainzEntity.RELEASE
        Tab.RELEASE_GROUPS -> MusicBrainzEntity.RELEASE_GROUP
        Tab.SERIES -> MusicBrainzEntity.SERIES
        Tab.WORKS -> MusicBrainzEntity.WORK
        Tab.DETAILS,
        Tab.RELATIONSHIPS,
        Tab.STATS,
        Tab.TRACKS,
        -> null
    }
}

fun MusicBrainzEntity.toTab(): Tab? {
    return when (this) {
        MusicBrainzEntity.AREA -> Tab.AREAS
        MusicBrainzEntity.ARTIST -> Tab.ARTISTS
        MusicBrainzEntity.EVENT -> Tab.EVENTS
        MusicBrainzEntity.GENRE -> Tab.GENRES
        MusicBrainzEntity.INSTRUMENT -> Tab.INSTRUMENTS
        MusicBrainzEntity.LABEL -> Tab.LABELS
        MusicBrainzEntity.PLACE -> Tab.PLACES
        MusicBrainzEntity.RECORDING -> Tab.RECORDINGS
        MusicBrainzEntity.RELEASE -> Tab.RELEASES
        MusicBrainzEntity.RELEASE_GROUP -> Tab.RELEASE_GROUPS
        MusicBrainzEntity.SERIES -> Tab.SERIES
        MusicBrainzEntity.WORK -> Tab.WORKS
        MusicBrainzEntity.COLLECTION,
        MusicBrainzEntity.URL,
        -> null
    }
}
