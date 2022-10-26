package ly.david.mbjc.ui.common

import androidx.annotation.StringRes
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.R

/**
 * Returns an appropriate string resource for this [MusicBrainzResource].
 */
@StringRes
fun MusicBrainzResource.getDisplayTextRes(): Int {
    return when (this) {
        MusicBrainzResource.AREA -> R.string.area
        MusicBrainzResource.ARTIST -> R.string.artist
        MusicBrainzResource.EVENT -> R.string.event
        MusicBrainzResource.GENRE -> R.string.genre
        MusicBrainzResource.INSTRUMENT -> R.string.instrument
        MusicBrainzResource.LABEL -> R.string.label
        MusicBrainzResource.PLACE -> R.string.place
        MusicBrainzResource.RECORDING -> R.string.recording
        MusicBrainzResource.RELEASE -> R.string.release
        MusicBrainzResource.RELEASE_GROUP -> R.string.release_group
        MusicBrainzResource.SERIES -> R.string.series
        MusicBrainzResource.WORK -> R.string.work
        MusicBrainzResource.URL -> R.string.url
    }
}
