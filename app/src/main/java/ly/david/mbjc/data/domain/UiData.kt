package ly.david.mbjc.data.domain

import ly.david.mbjc.data.network.MusicBrainzArtist
import ly.david.mbjc.data.network.MusicBrainzData
import ly.david.mbjc.data.network.MusicBrainzReleaseGroup

/**
 * Data that can be displayed in our app should extend this.
 */
sealed class UiData

/**
 * Represents the end of the list of [UiData] being displayed.
 */
object EndOfList : UiData()

/**
 * Represents a separator with [text] that can be inserted between two list items.
 */
class ListSeparator(val text: String) : UiData()

/**
 * Converts a [MusicBrainzData] that we got from the network to its UI version for display.
 *
 * We can map a [MusicBrainzData] to [UiData] but not the other way around because there are [UiData] such as
 * [EndOfList] that do not have a 1-to-t mapping. We could still do it, but the result will be nullable.
 *
 * It seems like this needs to be in the same directory as [UiData] or else it tells us to add an else branch.
 */
fun MusicBrainzData.toUiData(): UiData {
    return when (this) {
        is MusicBrainzArtist -> this.toUiArtist()
        is MusicBrainzReleaseGroup -> this.toUiReleaseGroup()
    }
}
