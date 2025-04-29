package ly.david.musicsearch.ui.common.topappbar

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.strings.AppStrings

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

fun Tab.toMusicBrainzEntity(): MusicBrainzEntity {
    return when (this) {
        Tab.ARTISTS -> MusicBrainzEntity.ARTIST
        Tab.EVENTS -> MusicBrainzEntity.EVENT
        Tab.LABELS -> MusicBrainzEntity.LABEL
        Tab.PLACES -> MusicBrainzEntity.PLACE
        Tab.RECORDINGS -> MusicBrainzEntity.RECORDING
        Tab.RELEASES -> MusicBrainzEntity.RELEASE
        Tab.RELEASE_GROUPS -> MusicBrainzEntity.RELEASE_GROUP
        Tab.WORKS -> MusicBrainzEntity.WORK
        else -> MusicBrainzEntity.ARTIST
    }
}

fun Tab.getCachedLocalOfRemoteStringFunction(strings: AppStrings): (Int, Int) -> String {
    return when (this) {
        Tab.ARTISTS -> strings.cachedArtists
        Tab.EVENTS -> strings.cachedEvents
        Tab.LABELS -> strings.cachedLabels
        Tab.PLACES -> strings.cachedPlaces
        Tab.RECORDINGS -> strings.cachedRecordings
        Tab.RELEASES -> strings.cachedReleases
        Tab.RELEASE_GROUPS -> strings.cachedReleaseGroups
        Tab.WORKS -> strings.cachedWorks
        else -> strings.cachedArtists
    }
}
