package ly.david.mbjc.ui.common

import androidx.annotation.StringRes
import ly.david.ui.common.R

/**
 * All possible tabs in MusicBrainz resource pages.
 */
internal enum class Tab(@StringRes val titleRes: Int) {
    DETAILS(R.string.details),
    EVENTS(R.string.events),
    PLACES(R.string.places),
    RECORDINGS(R.string.recordings),
    RELATIONSHIPS(R.string.relationships),
    RELEASES(R.string.releases),
    RELEASE_GROUPS(R.string.release_groups),
    STATS(R.string.stats),
    TRACKS(R.string.tracks)
}
