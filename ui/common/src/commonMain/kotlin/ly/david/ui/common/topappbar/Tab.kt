package ly.david.ui.common.topappbar

import ly.david.musicsearch.strings.AppStrings

/**
 * All possible tabs in MusicBrainz resource pages.
 */
enum class Tab {
    DETAILS,
    ARTISTS,
    EVENTS,
    LABELS,
    PLACES,
    RECORDINGS,
    RELATIONSHIPS,
    RELEASES,
    RELEASE_GROUPS,
    STATS,
    TRACKS,
    WORKS,
}

fun Tab.getTitle(strings: AppStrings): String {
    return when (this) {
        Tab.DETAILS -> strings.details
        Tab.ARTISTS -> strings.artists
        Tab.EVENTS -> strings.events
        Tab.LABELS -> strings.labels
        Tab.PLACES -> strings.places
        Tab.RECORDINGS -> strings.recordings
        Tab.RELATIONSHIPS -> strings.relationships
        Tab.RELEASES -> strings.releases
        Tab.RELEASE_GROUPS -> strings.releaseGroups
        Tab.STATS -> strings.stats
        Tab.TRACKS -> strings.tracks
        Tab.WORKS -> strings.works
    }
}
